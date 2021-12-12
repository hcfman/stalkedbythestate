package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.CertTools;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CertInstaller {
	static final Logger logger = LoggerFactory.getLogger(CertInstaller.class);
	static final Logger oplogger = LoggerFactory.getLogger("operations");
	private FreakApi freak;
	
	// true if inserting into the keyStore otherwise we wish to insert into the truststore
	boolean inKeyStore;
	private KeyStore keyStore;
	private String keyStorePassword;
	private KeyStore trustStore;
	private String trustStorePassword;
	private String tmpLocation;
	private final String filename = "import.crt";
	private final String SEP = File.separator;
	private CertificateFactory cf = null;
	private SbtsDeviceConfig sbtsConfig;
	
	private boolean trustcacerts = true;
	
    public CertInstaller(FreakApi freak, SbtsDeviceConfig sbtsConfig, String tmpLocation) throws Exception {
    	this.freak = freak;
    	this.sbtsConfig = sbtsConfig;
		this.tmpLocation = tmpLocation;

		keyStorePassword = sbtsConfig.getSettingsConfig().getKeystorePassword();
		trustStorePassword = sbtsConfig.getSettingsConfig().getTruststorePassword();

		keyStore = sbtsConfig.getCertificateConfig().getKeyStore();
		
		try {
			if (keyStore == null) {
				CertTools certTools = new CertTools(freak);
				keyStore = certTools.initialiseKeyStore(sbtsConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cannot initialize the keyStore: " + e.getMessage());
			oplogger.error("Cannot initialize the keyStore");
			throw new Exception("Cannot initialize the keyStore");
		}
		
		trustStore = sbtsConfig.getCertificateConfig().getTrustStore();
		
		try {
			if (trustStore == null) {
				CertTools certTools = new CertTools(freak);
				trustStore = certTools.initialiseTrustStore(sbtsConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cannot initialize the trustStore: " + e.getMessage());
			oplogger.error("Cannot initialize the trustStore");
			throw new Exception("Cannot initialize the trustStore");
		}
		
	}
    
    public synchronized void importCert(boolean inKeyStore, String alias, String certificateString) throws Exception {
    	if (logger.isDebugEnabled()) logger.debug("ImportCert, inKeyStore: " + inKeyStore);
    	this.inKeyStore = inKeyStore;
    	
		keyStore = sbtsConfig.getCertificateConfig().getKeyStore();
		
		if (keyStore == null) {
			CertTools certTools = new CertTools(freak);
			keyStore = certTools.initialiseKeyStore(sbtsConfig);
			if (logger.isDebugEnabled()) logger.debug("Initialised keyStore");
		}
		
		trustStore = sbtsConfig.getCertificateConfig().getTrustStore();
		
		if (trustStore == null) {
			CertTools certTools = new CertTools(freak);
			trustStore = certTools.initialiseTrustStore(sbtsConfig);
			if (logger.isDebugEnabled()) logger.debug("Initialised trustStore");
		}
		
    	
    	if (!inKeyStore && "sbts".equals(alias)) {
    		oplogger.error("Trying to insert the sbts certificate into the trustStore, this must go into the keyStore");
    		throw new Exception("Trying to insert the sbts certificate into the trustStore, this must go into the keyStore");
    	}
    	
		if ("sbts".equals(alias) && (!keyStore.containsAlias("sbts") || !keyStore.entryInstanceOf("sbts", KeyStore.PrivateKeyEntry.class)))
			throw new Exception("Trying to import a signed certificate when you haven't generated an unsigned one yet");
		
		if (logger.isDebugEnabled()) logger.debug("Store the certficate in a temporary file");

		freak.mountReadWrite();
		try {
			try (PrintWriter printWriter = new PrintWriter(new FileWriter(tmpLocation + SEP + filename))) {
				printWriter.print(certificateString);
			}

			InputStream in = new FileInputStream(tmpLocation + SEP + filename);
			
			try {
				if ("sbts".equals(alias))
					importReply(alias, in);
				else
					addTrustedCert(inKeyStore, alias, in);
				
				File storeFile = new File(freak.getSbtsBase() + SEP + (inKeyStore ? ("cacerts" + SEP + "keystore.jks") : ("certs" + SEP + "truststore.jks")));
				if (logger.isDebugEnabled()) logger.debug("Write store away: " + storeFile.toString());
				KeyStore currentStore = inKeyStore ? keyStore : trustStore;
				String password = inKeyStore ? keyStorePassword : trustStorePassword;
		        FileOutputStream outFile = new FileOutputStream(storeFile);
		        currentStore.store(outFile, password.toCharArray());
		        outFile.close();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			} finally {
				if (in != null)
					in.close();
			}
			
			if (logger.isDebugEnabled()) logger.debug("Added certificate");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("I/O error");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("I/O error");
		} finally {
			freak.mountReadonly();
		}
    }

	private boolean importReply(String alias, InputStream in) throws Exception {
		if (logger.isDebugEnabled()) logger.debug("Import reply");
		PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());

		Certificate userCert = keyStore.getCertificate(alias);

		if (userCert == null)
			throw new Exception("Certificate disappeared from the keyStore");

		// Read the certificates in the reply
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		Collection<? extends Certificate> c = cf.generateCertificates(in);
		if (c.isEmpty())
			throw new Exception("Reply has no certificates");

		Certificate[] replyCerts = c.toArray(new Certificate[c.size()]);
		Certificate[] newChain;
		if (replyCerts.length == 1) {
			// single-cert reply
			newChain = establishCertChain(userCert, replyCerts[0]);
		} else {
			// cert-chain reply (e.g., PKCS#7)
			newChain = validateReply(alias, userCert, replyCerts);
		}

		// Now store the newly established chain in the keystore. The new
		// chain replaces the old one.
		if (newChain != null) {
			keyStore.setKeyEntry(alias, privKey, keyStorePassword.toCharArray(), newChain);
			return true;
		} else {
			return false;
		}
	}

	private Certificate[] establishCertChain(Certificate userCert, Certificate certToVerify) throws Exception {
		if (userCert != null) {
			// Make sure that the public key of the certificate reply matches
			// the original public key in the keystore
			PublicKey origPubKey = userCert.getPublicKey();
			PublicKey replyPubKey = certToVerify.getPublicKey();
			if (!origPubKey.equals(replyPubKey))
				throw new Exception("Public keys in reply and keystore don't match");

			// If the two certs are identical, we're done: no need to import
			// anything
			if (certToVerify.equals(userCert))
				throw new Exception("Certificate reply and certificate in keystore are identical");
		}

		// Build a hash table of all certificates in the keystore.
		// Use the subject distinguished name as the key into the hash table.
		// All certificates associated with the same subject distinguished
		// name are stored in the same hash table entry as a vector.
		Hashtable<Principal, Vector<Certificate>> certs = null;
		if (keyStore.size() > 0) {
			certs = new Hashtable<Principal, Vector<Certificate>>(11);
			keystorecerts2Hashtable(keyStore, certs);
		}

		if (trustStore != null && trustStore.size() > 0) {
			if (certs == null)
				certs = new Hashtable<Principal, Vector<Certificate>>(11);
			keystorecerts2Hashtable(trustStore, certs);
		}

		// start building chain
		Vector<Certificate> chain = new Vector<Certificate>(2);
		if (buildChain((X509Certificate) certToVerify, chain, certs)) {
			Certificate[] newChain = new Certificate[chain.size()];
			// buildChain() returns chain with self-signed root-cert first and
			// user-cert last, so we need to invert the chain before we store
			// it
			int j = 0;
			for (int i = chain.size() - 1; i >= 0; i--) {
				newChain[j] = chain.elementAt(i);
				j++;
			}
			return newChain;
		} else {
			throw new Exception("Failed to establish chain from reply");
		}
	}

	/**
     * Returns true if the given certificate is trusted, false otherwise.
     */
    private boolean isTrusted(Certificate cert) throws Exception {
        if (keyStore.getCertificateAlias(cert) != null) {
            return true; // found in own keystore
        }
        if (trustcacerts && (trustStore != null)
                && (trustStore.getCertificateAlias(cert) != null)) {
            return true; // found in CA keystore
        }
        return false;
    }


	private Certificate[] validateReply(String alias, Certificate userCert,
                                        Certificate[] replyCerts) throws Exception {
		// order the certs in the reply (bottom-up).
		// we know that all certs in the reply are of type X.509, because
		// we parsed them using an X.509 certificate factory
		int i;
		PublicKey userPubKey = userCert.getPublicKey();
		for (i = 0; i < replyCerts.length; i++) {
			if (userPubKey.equals(replyCerts[i].getPublicKey())) {
				break;
			}
		}
		if (i == replyCerts.length)
			throw new Exception("Certificate reply does not contain the matching public key");

		Certificate tmpCert = replyCerts[0];
		replyCerts[0] = replyCerts[i];
		replyCerts[i] = tmpCert;
		Principal issuer = ((X509Certificate) replyCerts[0]).getIssuerDN();

		for (i = 1; i < replyCerts.length - 1; i++) {
			// find a cert in the reply whose "subject" is the same as the
			// given "issuer"
			int j;
			for (j = i; j < replyCerts.length; j++) {
				Principal subject;
				subject = ((X509Certificate) replyCerts[j]).getSubjectDN();
				if (subject.equals(issuer)) {
					tmpCert = replyCerts[i];
					replyCerts[i] = replyCerts[j];
					replyCerts[j] = tmpCert;
					issuer = ((X509Certificate) replyCerts[i]).getIssuerDN();
					break;
				}
			}
			if (j == replyCerts.length)
				throw new Exception("The certificate chain is not complete in the imported reply");
		}

		// now verify each cert in the ordered chain
		for (i = 0; i < replyCerts.length - 1; i++) {
			PublicKey pubKey = replyCerts[i + 1].getPublicKey();
			try {
				replyCerts[i].verify(pubKey);
			} catch (Exception e) {
				throw new Exception("Can't verify the certificate chain in the reply: " + e.getMessage());
			}
		}

		boolean noprompt = false;
        if (noprompt) {
			return replyCerts;
        }

        // do we trust the (root) cert at the top?
        Certificate topCert = replyCerts[replyCerts.length - 1];
        if (!isTrusted(topCert)) {
            boolean verified = false;
            Certificate rootCert = null;
            if (trustcacerts && (trustStore != null)) {
                for (Enumeration<String> aliases = trustStore.aliases(); aliases
                        .hasMoreElements();) {
                    String name = aliases.nextElement();
                    rootCert = trustStore.getCertificate(name);
                    if (rootCert != null) {
                        try {
                            topCert.verify(rootCert.getPublicKey());
                            verified = true;
                            break;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (!verified) {
                System.err.println();
                System.err.println("Top-level certificate in reply:\n");
//                printX509Cert((X509Certificate) topCert, System.out);
                System.err.println();
                System.err.print("... is not trusted. ");
                String reply = "NO";
                if ("NO".equals(reply)) {
                    return null;
                }
            } else {
                if (!isSelfSigned((X509Certificate) topCert)) {
                    // append the (self-signed) root CA cert to the chain
                    Certificate[] tmpCerts = new Certificate[replyCerts.length + 1];
                    System.arraycopy(replyCerts, 0, tmpCerts, 0,
                            replyCerts.length);
                    tmpCerts[tmpCerts.length - 1] = rootCert;
                    replyCerts = tmpCerts;
                }
            }
        }

        return replyCerts;

	}

	private void keystorecerts2Hashtable(KeyStore ks,
			Hashtable<Principal, Vector<Certificate>> hash) throws Exception {

		for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements();) {
			String alias = aliases.nextElement();
			Certificate cert = ks.getCertificate(alias);
			if (cert != null) {
				Principal subjectDN = ((X509Certificate) cert).getSubjectDN();
				Vector<Certificate> vec = hash.get(subjectDN);
				if (vec == null) {
					vec = new Vector<Certificate>();
					vec.addElement(cert);
				} else {
					if (!vec.contains(cert))
						vec.addElement(cert);
				}
				hash.put(subjectDN, vec);
			}
		}
	}

	/**
	 * Recursively tries to establish chain from pool of trusted certs.
	 * 
	 * @param certToVerify
	 *            the cert that needs to be verified.
	 * @param chain
	 *            the chain that's being built.
	 * @param certs
	 *            the pool of trusted certs
	 * 
	 * @return true if successful, false otherwise.
	 */
	private boolean buildChain(X509Certificate certToVerify, Vector<Certificate> chain,
                               Hashtable<Principal, Vector<Certificate>> certs) {
		Principal subject = certToVerify.getSubjectDN();
		Principal issuer = certToVerify.getIssuerDN();
		if (subject.equals(issuer)) {
			// reached self-signed root cert;
			// no verification needed because it's trusted.
			chain.addElement(certToVerify);
			return true;
		}

		// Get the issuer's certificate(s)
		Vector<Certificate> vec = certs.get(issuer);
		if (vec == null)
			return false;

		// Try out each certificate in the vector, until we find one
		// whose public key verifies the signature of the certificate
		// in question.
		for (Enumeration<Certificate> issuerCerts = vec.elements(); issuerCerts.hasMoreElements();) {
			X509Certificate issuerCert = (X509Certificate) issuerCerts.nextElement();
			PublicKey issuerPubKey = issuerCert.getPublicKey();
			try {
				certToVerify.verify(issuerPubKey);
			} catch (Exception e) {
				continue;
			}
			if (buildChain(issuerCert, chain, certs)) {
				chain.addElement(certToVerify);
				return true;
			}
		}
		return false;
	}

    /**
     * Returns true if the certificate is self-signed, false otherwise.
     */
    private boolean isSelfSigned(X509Certificate cert) {
        return cert.getSubjectDN().equals(cert.getIssuerDN());
    }


	private boolean addTrustedCert(boolean inKeyStore, String alias, InputStream in) throws Exception {
		KeyStore currentStore = inKeyStore ? keyStore : trustStore;
		if (logger.isDebugEnabled()) logger.debug("addTrustedCert");
		if (alias == null)
			throw new Exception("Must specify alias");
		
		if (currentStore.containsAlias(alias)) {
			if (logger.isDebugEnabled()) logger.debug("Certificate not imported, alias <alias> already exists");
			throw new Exception("Certificate not imported, alias <alias> already exists");
		}

		CertificateFactory cf = CertificateFactory.getInstance("X509");

		// Read the certificate
		X509Certificate cert = null;
		try {
			cert = (X509Certificate) cf.generateCertificate(in);
		} catch (ClassCastException cce) {
			throw new Exception("Input not an X.509 certificate");
		} catch (CertificateException ce) {
			throw new Exception("Input not an X.509 certificate");
		}

		// if certificate is self-signed, make sure it verifies
		boolean selfSigned = false;
		if (isSelfSigned(cert)) {
			cert.verify(cert.getPublicKey());
			selfSigned = true;
			if (logger.isDebugEnabled()) logger.debug("Certificate is self signed and verifies");
		}

//        if (noprompt) {
//			currentStore.setCertificateEntry(alias, cert);
//			return true;
//		}

		// check if cert already exists in keystore
		String reply = null;
		String trustalias = currentStore.getCertificateAlias(cert);
		if (trustalias != null) {
			throw new Exception("Certificate already exists in " + (inKeyStore ? "keyStore" : "trustStore") +
					" with alias called " + trustalias + " not overriding. Delete from the store first");
		} else if (selfSigned) {
			if (logger.isDebugEnabled()) logger.debug("Certificate is self-signed");
			if (inKeyStore && (trustStore != null)
					&& ((trustalias = trustStore.getCertificateAlias(cert)) != null)) {
				throw new Exception("Certificate already exists in trustStore under alias " + trustalias + " not overriding. Delete from the store first");
//				Object[] source = { trustalias };
//				reply = "NO";
			}
			if (trustalias == null) {
				
				// Print the cert and ask user if they really want to add
				// it to their keystore
//				printX509Cert(cert, System.out);
				reply = "YES";
			}
		} else {
			if (logger.isDebugEnabled()) logger.debug("Certificate is not self signed");
		}
		if (reply != null) {
			if ("YES".equals(reply)) {
				if (logger.isDebugEnabled()) logger.debug("Store cert anyway");
				currentStore.setCertificateEntry(alias, cert);
				return true;
			} else {
				if (logger.isDebugEnabled()) logger.debug("");
				return false;
			}
		}

		if (logger.isDebugEnabled()) logger.debug("Try and establish trust");
		// Try to establish trust chain
		try {
			if (logger.isDebugEnabled()) logger.debug("Establish certificate chain");
			Certificate[] chain = establishCertChain(null, cert);
			if (chain != null) {
				if (logger.isDebugEnabled()) logger.debug("Established chain, add it");
				currentStore.setCertificateEntry(alias, cert);
				if (logger.isDebugEnabled()) logger.debug("Everything is good, adding cert");
				return true;
			} else {
				if (logger.isDebugEnabled()) logger.debug("Certificate chain is null");
			}
		} catch (Exception e) {
			throw new Exception("Cannot establish certificate chain: " + e.getMessage());
//			// Print the cert and ask user if they really want to add it to
//			// their keystore
////			printX509Cert(cert, System.out);
//			reply = "NO";
//			if ("YES".equals(reply)) {
//				currentStore.setCertificateEntry(alias, cert);
//				return true;
//			} else {
//				return false;
//			}
		}

		return false;
	}

}
