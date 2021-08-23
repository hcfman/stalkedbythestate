package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.*;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@WebServlet(urlPatterns = {"/certificatesgetjson"})
public class CertificatesGetJSON extends HttpServlet {
    private static final long serialVersionUID = -2277729964948177781L;
    private static final Logger logger = Logger
            .getLogger(CertificatesGetJSON.class);
    private SbtsDeviceConfig sbtsConfig;
    FreakApi freak;

    @Override
    public void init() throws ServletException {
//		freak = Freak.getInstance();
    }

    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    private String getCertFingerPrint(String mdAlg, Certificate cert)
            throws Exception {
        byte[] encCertInfo = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance(mdAlg);
        byte[] digest = md.digest(encCertInfo);
        return toHexString(digest);
    }

    private boolean initKeyStore(CertificatesJSON certificatesJSON) {
        if (logger.isDebugEnabled())
            logger.debug("initKeyStore");
        KeyStore keyStore = sbtsConfig.getCertificateConfig().getKeyStore();

        if (keyStore == null) {
            CertTools certTools = new CertTools(freak);
            try {
                keyStore = certTools.initialiseKeyStore(sbtsConfig);
                sbtsConfig.getCertificateConfig().setKeyStore(keyStore);
            } catch (Exception e) {
                logger.error("Exception initialising keystore: "
                        + e.getMessage());
                e.printStackTrace();

                certificatesJSON.setResult(false);
                certificatesJSON.getMessages().add("Can't read the keystore");
                return false;
            }
        }

        try {
            // Save all certificates so we can look them up by the DN of the subject
            Map<String, X509Certificate> allCertificates = new HashMap<String, X509Certificate>();
            for (Enumeration<String> entry = keyStore.aliases(); entry.hasMoreElements(); ) {
                String alias = entry.nextElement();
                if (!keyStore.containsAlias(alias))
                    continue;

                Certificate cert = keyStore.getCertificate(alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) cert;
                    allCertificates.put(x509Certificate.getSubjectDN().toString(), (X509Certificate) cert);
                }
            }

            // Now build up the list of all certificates present, if the "sbts" alias exists, store it's chain json
            for (Enumeration<String> entry = keyStore.aliases(); entry.hasMoreElements(); ) {
                String alias = entry.nextElement();
                if (!keyStore.containsAlias(alias))
                    continue;

                Collection<Certificate> chain = new ArrayList<Certificate>();
                Certificate[] storeChain = keyStore.getCertificateChain(alias);
                CertificateChainJSON certificateChainJSON = new CertificateChainJSON();
                certificateChainJSON.setAlias(alias);

                if (logger.isDebugEnabled())
                    logger.debug("Chain: " + chain);
                if (logger.isDebugEnabled())
                    logger.debug("Alias: " + alias);

                Certificate mainCert = keyStore.getCertificate(alias);

                if (storeChain != null && storeChain.length > 0) {
                    chain.addAll(Arrays.asList(storeChain));

                    Certificate lastCert = storeChain[storeChain.length - 1];
                    if (lastCert instanceof X509Certificate &&
                            !((X509Certificate) lastCert).getIssuerDN().equals(((X509Certificate) lastCert).getSubjectDN())) {
                        X509Certificate x509Certificate = (X509Certificate) lastCert;
                        Certificate certificate = allCertificates.get(x509Certificate.getIssuerDN().toString());
                        if (certificate != null)
                            chain.add(certificate);
                    }
                } else {
                    // Now add the root, which is not present in the chain
                    chain.add(mainCert);
                }
                String entryType;
                if (keyStore.entryInstanceOf(alias,
                        KeyStore.PrivateKeyEntry.class)) {
                    entryType = "Private Key";
                } else {
                    entryType = "Public Certificate";
                }
                certificateChainJSON.setEntryType(entryType);

                for (Certificate cert : chain) {
                    CertificateJSON certificateJSON = null;


                    if (cert instanceof X509Certificate) {
                        X509Certificate x509Certificate = (X509Certificate) cert;

                        CertificateJSONbuilder certificateJSONbuilder = new CertificateJSONbuilder();
                        certificateJSON = certificateJSONbuilder.build(alias, x509Certificate);

                    }

                    certificateChainJSON.getCertificateChain().add(
                            certificateJSON);
                }
                Collections.reverse(certificateChainJSON.getCertificateChain());

                if (alias.equals("sbts")) {
                    certificatesJSON.setCertificate(certificateChainJSON);
                } else
                    certificatesJSON.getKeystore().add(
                            certificateChainJSON.getCertificateChain().get(0));
            }
        } catch (KeyStoreException e) {
            logger.error("KeyStore exception thrown reading keystore: "
                    + e.getMessage());
            e.printStackTrace();
        }

        if (logger.isDebugEnabled())
            logger.debug("Initialised keyStore, returning");
        return true;
    }

    private boolean initTrustStore(CertificatesJSON certificatesJSON) {
        if (logger.isDebugEnabled())
            logger.debug("initTrustStore");
        KeyStore trustStore = sbtsConfig.getCertificateConfig().getTrustStore();

        if (trustStore == null) {
            if (logger.isDebugEnabled())
                logger.debug("Truststore is null, go fetch");
            CertTools certTools = new CertTools(freak);
            try {
                trustStore = certTools.initialiseTrustStore(sbtsConfig);
                if (logger.isDebugEnabled())
                    logger.debug("Retrieved truststore, set it");
                sbtsConfig.getCertificateConfig().setTrustStore(trustStore);
            } catch (Exception e) {
                logger.error("Exception initialising truststore: "
                        + e.getMessage());
                e.printStackTrace();

                certificatesJSON.setResult(false);
                certificatesJSON.getMessages().add("Can't read the truststore");
                return false;
            }
        } else {
            if (logger.isDebugEnabled())
                logger.debug("Truststore is not null");
        }

        try {
            for (Enumeration<String> e = trustStore.aliases(); e
                    .hasMoreElements(); ) {
                String alias = e.nextElement();
                if (!trustStore.containsAlias(alias))
                    continue;

                Certificate[] chain = trustStore.getCertificateChain(alias);
                CertificateChainJSON certificateChainJSON = new CertificateChainJSON();
                certificateChainJSON.setAlias(alias);

                if (logger.isDebugEnabled())
                    logger.debug("Chain: " + chain);
                if (logger.isDebugEnabled())
                    logger.debug("Alias: " + alias);

                Certificate mainCert = trustStore.getCertificate(alias);
                String entryType;
                if (trustStore.entryInstanceOf(alias,
                        KeyStore.PrivateKeyEntry.class)) {
                    chain = trustStore.getCertificateChain(alias);
                    entryType = "Private Key";
                } else {
                    chain = new Certificate[]{mainCert};
                    entryType = "Public Certificate";
                }
                certificateChainJSON.setEntryType(entryType);

                for (Certificate cert : chain) {
                    CertificateJSON certificateJSON = new CertificateJSON();

                    if (cert instanceof X509Certificate) {
                        X509Certificate x509Certificate = (X509Certificate) cert;

                        if (logger.isDebugEnabled())
                            logger.debug("It's X509: "
                                    + x509Certificate.getNotAfter().toString());
                        if (logger.isDebugEnabled())
                            logger.debug("getIssuerDN().toString(): "
                                    + x509Certificate.getIssuerDN().toString());

                        certificateJSON.setAlias(alias);
                        certificateJSON.setValidFrom(x509Certificate
                                .getNotBefore().toString());
                        certificateJSON.setValidTo(x509Certificate
                                .getNotAfter().toString());

                        String issuerDnString = x509Certificate.getIssuerDN()
                                .toString();
                        if (logger.isDebugEnabled())
                            logger.debug("dn: " + issuerDnString);
                        Dn issuer = Dn.parseDn(issuerDnString);
                        certificateJSON.setIssuer(issuer);

                        String ownerDnString = x509Certificate.getSubjectDN()
                                .toString();
                        if (logger.isDebugEnabled())
                            logger.debug("dn: " + ownerDnString);
                        Dn owner = Dn.parseDn(ownerDnString);
                        certificateJSON.setOwner(owner);

                        if (trustStore.isKeyEntry(alias)) {
                            Key key = trustStore.getKey(alias, sbtsConfig
                                    .getSettingsConfig().getKeystorePassword()
                                    .toCharArray());
                        }
                        certificateJSON.setKeyAlg(x509Certificate
                                .getPublicKey().getAlgorithm());
                        if (x509Certificate.getPublicKey() instanceof RSAPublicKey) {
                            RSAPublicKey rsaPublicKey = (RSAPublicKey) x509Certificate
                                    .getPublicKey();
                            certificateJSON.setKeySize(rsaPublicKey
                                    .getModulus().bitLength());
                        }
                        certificateJSON.setSerial(x509Certificate
                                .getSerialNumber().toString(16).toUpperCase()
                                .replaceAll("((?!^.)..)(?=(?:..)*$)", ":$1")
                                .replaceAll("^(.(?:...)*)$", "0$1"));

                        try {
                            certificateJSON
                                    .setFingerprint("MD5: "
                                            + getCertFingerPrint("MD5",
                                            x509Certificate)
                                            + "\nSHA: "
                                            + getCertFingerPrint("SHA",
                                            x509Certificate));
                        } catch (Exception e1) {
                            logger.error("Exception printing cert fingerprint");
                            e1.printStackTrace();
                        }
                    }

                    certificateJSON.setValidFrom(trustStore.getCreationDate(
                            alias).toString());
                    certificateChainJSON.getCertificateChain().add(
                            certificateJSON);
                }
                Collections.reverse(certificateChainJSON.getCertificateChain());

                certificatesJSON.getTruststore().add(
                        certificateChainJSON.getCertificateChain().get(0));
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm thrown reading keystore: "
                    + e.getMessage());
            e.printStackTrace();
        } catch (KeyStoreException e) {
            logger.error("KeyStore exception thrown reading keystore: "
                    + e.getMessage());
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            logger.error("Unrecoverable key error reading keystore");
            e.printStackTrace();
        }

        if (logger.isDebugEnabled())
            logger.debug("Initialised truststore returning");
        return true;
    }

    @RequestMapping("/certificatesgetjson")
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        if (freak == null)
            freak = Freak.getInstance();

        response.setContentType("application/json");

        sbtsConfig = freak.getSbtsConfig();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        PrintWriter out = response.getWriter();
        CertificatesJSON certificatesJSON = new CertificatesJSON(true);
        initKeyStore(certificatesJSON);
        if (logger.isDebugEnabled())
            logger.debug("Now initialise truststore");
        initTrustStore(certificatesJSON);

        // We may use freak names now as user adds these
        // for (FreakDevice freakDevice :
        // sbtsConfig.getFreakConfig().getFreakMap()
        // .values())
        // certificatesJSON.getDisallowedAliases().add(freakDevice.getName());

        out.print(gson.toJson(certificatesJSON));
    }
}
