package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.CertTools;
import com.stalkedbythestate.sbts.json.CsrJSON;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

@WebServlet(urlPatterns={"/csrgetjson"})
public class CsrGetJSON extends HttpServlet {
	private static final long serialVersionUID = 352453434439740449L;
	private static final Logger logger = Logger.getLogger(CsrGetJSON.class);
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		final SbtsDeviceConfig sbtsConfig = freak.getSbtsConfig();

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();

		final PrintWriter out = response.getWriter();
		final CsrJSON csrJSON = new CsrJSON();

		if (logger.isDebugEnabled())
			logger.debug("Get keystore");
		KeyStore keyStore = sbtsConfig.getCertificateConfig().getKeyStore();
		Security.addProvider(new BouncyCastleProvider());

		if (logger.isDebugEnabled())
			logger.debug("keyStore: " + keyStore);
		if (keyStore == null) {
			final CertTools certTools = new CertTools(freak);
			try {
				keyStore = certTools.initialiseKeyStore(sbtsConfig);
				sbtsConfig.getCertificateConfig().setKeyStore(keyStore);
			} catch (final Exception e) {
				logger.error("Exception initialising keystore: " + e.getMessage());
				e.printStackTrace();

				csrJSON.getMessages().add("Can't read the keystore");
				out.print(gson.toJson(csrJSON));
				return;
			}
		}

		final Certificate cert;
		try {
			cert = keyStore.getCertificate("sbts");
			final String storePassword = sbtsConfig.getSettingsConfig().getKeystorePassword();

			if (!keyStore.entryInstanceOf("sbts", KeyStore.PrivateKeyEntry.class)
					&& !keyStore.entryInstanceOf("sbts", KeyStore.SecretKeyEntry.class)) {
				logger.error("Alias sbts has no key");

				csrJSON.getMessages().add("Problem with the certificate, try re-generating it");
				out.print(gson.toJson(csrJSON));
				return;
			}

			final byte[] encoded = cert.getEncoded();
			final ByteArrayInputStream bis = new ByteArrayInputStream(encoded);
			final java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
			final X509Certificate X509Cert = (X509Certificate) cf.generateCertificate(bis);

			final X500Name x500name = new JcaX509CertificateHolder(X509Cert).getSubject();

			final PrivateKey privKey = (PrivateKey) keyStore.getKey("sbts", storePassword.toCharArray());
			final ContentSigner signGen = new JcaContentSignerBuilder("SHA512withRSA").build(privKey);

			final PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(x500name, X509Cert.getPublicKey());
			final PKCS10CertificationRequest csr = builder.build(signGen);

			final StringBuilder sb = new StringBuilder();
			sb.append("-----BEGIN NEW CERTIFICATE REQUEST-----\n");

			// Use below in Java 8 for the csr encoding
			sb.append(Base64.getEncoder().encodeToString(csr.getEncoded()));
			sb.append("\n-----END NEW CERTIFICATE REQUEST-----\n");
			
			final StringWriter pemStrWriter = new StringWriter();
			final JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(pemStrWriter);
			jcaPEMWriter.writeObject(csr);
			jcaPEMWriter.close();

			csrJSON.setCsr(pemStrWriter.toString());
		} catch (final KeyStoreException e) {
			logger.error("Can't get certificate for alias sbts");
			e.printStackTrace();

			csrJSON.getMessages().add("Certificate does not yet exist, try re-generating it");
			out.print(gson.toJson(csrJSON));
			return;
		} catch (final NoSuchAlgorithmException e) {
			logger.error("No Such algorithm: " + e.getMessage());
			e.printStackTrace();

			csrJSON.getMessages().add("Problem with the certificate, try re-generating it");
			out.print(gson.toJson(csrJSON));
			return;
		} catch (final CertificateException e) {
			logger.error("Certificate Exception: " + e.getMessage());
			e.printStackTrace();

			csrJSON.getMessages().add("Problem with the certificate, try re-generating it");
			out.print(gson.toJson(csrJSON));
			return;
		} catch (final UnrecoverableKeyException e) {
			logger.error("Un recoverable key exception: " + e.getMessage());
			e.printStackTrace();

			csrJSON.getMessages().add("Problem with the certificate, try re-generating it");
			out.print(gson.toJson(csrJSON));
			return;
		} catch (final OperatorCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		csrJSON.setResult(true);
		out.print(gson.toJson(csrJSON));
	}
}
