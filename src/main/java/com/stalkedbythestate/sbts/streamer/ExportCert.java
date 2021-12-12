package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.CertTools;
import com.stalkedbythestate.sbts.json.CsrJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;
import java.security.cert.X509Certificate;

public class ExportCert {
	private static final Logger logger = LoggerFactory.getLogger(ExportCert.class);
	private FreakApi freak;

	public ExportCert(FreakApi freak) {
		this.freak = freak;
	}

	public CsrJSON export(SbtsDeviceConfig sbtsConfig, String alias, boolean isKeyStore) {
		if (logger.isDebugEnabled())
			logger.debug("Alias: " + alias);
		CsrJSON exportJSON = new CsrJSON();

		if (logger.isDebugEnabled())
			logger.debug("Get keystore");
		KeyStore keyStore;
		Security.addProvider(new BouncyCastleProvider());
		if (isKeyStore) {
			if (logger.isDebugEnabled())
				logger.debug("Fetching from the keyStore");
			keyStore = sbtsConfig.getCertificateConfig().getKeyStore();

			if (keyStore == null) {
				CertTools certTools = new CertTools(freak);
				try {
					keyStore = certTools.initialiseKeyStore(sbtsConfig);
					sbtsConfig.getCertificateConfig().setKeyStore(keyStore);
				} catch (Exception e) {
					logger.error("Exception initialising keystore: " + e.getMessage());
					e.printStackTrace();

					exportJSON.getMessages().add("Can't read the keystore");
					return exportJSON;
				}
			}

		} else {
			if (logger.isDebugEnabled())
				logger.debug("Fetching from the truststore");
			keyStore = sbtsConfig.getCertificateConfig().getTrustStore();

			if (keyStore == null) {
				CertTools certTools = new CertTools(freak);
				try {
					keyStore = certTools.initialiseTrustStore(sbtsConfig);
					sbtsConfig.getCertificateConfig().setTrustStore(keyStore);
				} catch (Exception e) {
					logger.error("Exception initialising truststore: " + e.getMessage());
					e.printStackTrace();

					exportJSON.getMessages().add("Can't read the truststore");
					return exportJSON;
				}
			}

		}

		X509Certificate cert = null;
		if (logger.isDebugEnabled())
			logger.debug("alias: " + alias);
		try {
			cert = (X509Certificate) keyStore.getCertificate(alias);
			if (logger.isDebugEnabled())
				logger.debug("cert: " + cert);
			StringWriter pemStrWriter = new StringWriter();
			JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(pemStrWriter);
			jcaPEMWriter.writeObject(cert);
			jcaPEMWriter.close();

			exportJSON.setCsr(pemStrWriter.toString());
		} catch (KeyStoreException | IOException e) {
			e.printStackTrace();
			logger.error("Certificate encoding exception: " + e.getMessage());
			exportJSON.getMessages().add("Problem with the certificate, try re-generating it");
			return exportJSON;
		}

		exportJSON.setResult(true);

		return exportJSON;
	}
}
