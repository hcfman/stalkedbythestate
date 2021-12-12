package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.CertTools;
import com.stalkedbythestate.sbts.json.CertificateJSON;
import com.stalkedbythestate.sbts.json.EditCertsJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@WebServlet(urlPatterns={"/editcerts"})
public class EditCerts extends HttpServlet {
	private static final long serialVersionUID = 1960213686411485822L;
	private static final Logger logger = LoggerFactory.getLogger(EditCerts.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	void initialiseKeyStore(KeyStore ks) throws Exception {
		char SEP = File.separatorChar;
		File truststoreFile = new File(freak.getSbtsBase() + SEP + "certs" + SEP
				+ "truststore.jks");
		ks = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = sbtsConfig.getSettingsConfig().getTruststorePassword();
		if (truststoreFile.canRead()) {
			FileInputStream fileInputStream = new FileInputStream(
					truststoreFile);
			ks.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
		} else {
			ks.load(null, null);
		}
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		response.setContentType("application/json");

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		String body = stringBuilder.toString();

		if (logger.isDebugEnabled())
			logger.debug("Body: " + body);

		Gson fromGson = new Gson();
		EditCertsJSON editCertsJSON = fromGson.fromJson(body,
				EditCertsJSON.class);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("actionsJSON has become: "
					+ gson.toJson(editCertsJSON));

		PrintWriter out = response.getWriter();

		// Don't store if updating
		if (freak.getUpdating().get()) {
			editCertsJSON.setKeystore(null);
			editCertsJSON.setTruststore(null);
			editCertsJSON.getMessages().add(
					"Can't save certificates, an update is in progress");
			out.print(gson.toJson(editCertsJSON));

			return;
		}

		synchronized (freak) {
			try {

				freak.mountReadWrite();
				opLogger.info("Installing certificate");

				// editCert(certificateList);
				if (logger.isDebugEnabled())
					logger.debug("Successfully installed cert");
				KeyStore keyStore = sbtsConfig.getCertificateConfig()
						.getKeyStore();

				if (logger.isDebugEnabled())
					logger.debug("keyStore: " + keyStore);
				if (keyStore == null) {
					CertTools certTools = new CertTools(freak);
					try {
						keyStore = certTools.initialiseKeyStore(sbtsConfig);
						sbtsConfig.getCertificateConfig().setKeyStore(keyStore);
					} catch (Exception e) {
						logger.error(
								"Exception initialising keystore: "
										+ e.getMessage(), e);

						editCertsJSON.setKeystore(null);
						editCertsJSON.setTruststore(null);
						editCertsJSON.getMessages().add(
								"Can't read the keystore");
						out.print(gson.toJson(editCertsJSON));
						return;
					}
				}

				KeyStore trustStore = sbtsConfig.getCertificateConfig()
						.getTrustStore();

				if (logger.isDebugEnabled())
					logger.debug("trustStore: " + trustStore);
				if (trustStore == null) {
					CertTools certTools = new CertTools(freak);
					try {
						trustStore = certTools.initialiseTrustStore(sbtsConfig);
						sbtsConfig.getCertificateConfig().setTrustStore(
								trustStore);
					} catch (Exception e) {
						logger.error("Exception initialising keystore: "
								+ e.getMessage());
						e.printStackTrace();

						editCertsJSON.setKeystore(null);
						editCertsJSON.setTruststore(null);
						editCertsJSON.getMessages().add(
								"Can't read the truststore");
						out.print(gson.toJson(editCertsJSON));
						return;
					}
				}

				if (editCertsJSON.getKeystore() == null) {
					editCertsJSON.setKeystore(null);
					editCertsJSON.setTruststore(null);
					editCertsJSON.getMessages().add(
							"Was passed an invalid keystore list");
					out.print(gson.toJson(editCertsJSON));
					return;
				}

				if (editCertsJSON.getTruststore() == null) {
					editCertsJSON.setKeystore(null);
					editCertsJSON.setTruststore(null);
					editCertsJSON.getMessages().add(
							"was passed an invalid truststore list");
					out.print(gson.toJson(editCertsJSON));
					return;
				}

				Set<String> currentKeyStoreSet = new HashSet<String>();
				for (CertificateJSON certificateJSON : editCertsJSON
						.getKeystore())
					currentKeyStoreSet.add(certificateJSON.getAlias());

				Set<String> currentTrustStoreSet = new HashSet<String>();
				for (CertificateJSON certificateJSON : editCertsJSON
						.getTruststore())
					currentTrustStoreSet.add(certificateJSON.getAlias());

				for (Enumeration<String> e = keyStore.aliases(); e
						.hasMoreElements();) {
					String alias = e.nextElement();
					if (!keyStore.containsAlias(alias) || alias.equals("sbts"))
						continue;
					if (!currentKeyStoreSet.contains(alias)) {
						if (logger.isDebugEnabled())
							logger.debug("Removing " + alias
									+ " from the keystore");
						keyStore.deleteEntry(alias);
					}
				}

				for (Enumeration<String> e = trustStore.aliases(); e
						.hasMoreElements();) {
					String alias = e.nextElement();
					if (!trustStore.containsAlias(alias) || alias.equals("sbts"))
						continue;
					if (!currentTrustStoreSet.contains(alias)) {
						if (logger.isDebugEnabled())
							logger.debug("Removing " + alias
									+ " from the truststore");
						trustStore.deleteEntry(alias);
					}
				}

				char SEP = File.separatorChar;
				File keystoreFile = new File(freak.getSbtsBase() + SEP
						+ "cacerts" + SEP + "keystore.jks");

				String keystorePassword = sbtsConfig.getSettingsConfig()
						.getKeystorePassword();
				FileOutputStream keystoreStream = new FileOutputStream(
						keystoreFile);
				keyStore.store(keystoreStream, keystorePassword.toCharArray());
				keystoreStream.close();

				editCertsJSON.setResult(true);

				File truststoreFile = new File(freak.getSbtsBase() + SEP
						+ "certs" + SEP + "truststore.jks");

				String truststorePassword = sbtsConfig.getSettingsConfig()
						.getTruststorePassword();
				FileOutputStream truststoreStream = new FileOutputStream(
						truststoreFile);
				trustStore.store(truststoreStream,
						truststorePassword.toCharArray());
				truststoreStream.close();

				editCertsJSON.setResult(true);

			} catch (Exception e) {
				logger.error("Can't install certificate: " + e.getMessage());
				e.printStackTrace();
				if (logger.isDebugEnabled())
					logger.debug("About to add the bad message");
				editCertsJSON.getMessages()
						.add("Failed to install certificate");
				opLogger.error("Failed to install certificates: "
						+ e.getMessage());
			} finally {
				freak.mountReadonly();
			}
		}

		editCertsJSON.setKeystore(null);
		editCertsJSON.setTruststore(null);

		out.print(gson.toJson(editCertsJSON));
	}
}
