package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class CertTools {
	static final Logger logger = Logger.getLogger(CertTools.class);
	private FreakApi freak;
	
	public CertTools(FreakApi freak) {
		this.freak = freak;
	}

	public KeyStore initialiseKeyStore(SbtsDeviceConfig sbtsConfig) throws Exception {
		if (logger.isDebugEnabled()) logger.debug("initialiseKeyStore");
		char SEP = File.separatorChar;
		File keystoreFile = new File(freak.getSbtsBase() + SEP + "cacerts" + SEP + "keystore.jks");
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = sbtsConfig.getSettingsConfig().getKeystorePassword();
		if (logger.isDebugEnabled()) logger.debug("keystorePassword: " + password);
		if (keystoreFile.canRead()) {
			if (logger.isDebugEnabled()) logger.debug("Keystore (" + keystoreFile + ") exists, reading");
			FileInputStream fileInputStream = new FileInputStream(keystoreFile);
			ks.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			if (logger.isDebugEnabled()) logger.debug("Returning and ks is: " + ks);
		} else {
			if (logger.isDebugEnabled()) logger.debug("Keystore doesn't exist, creating");
			ks.load(null, null);
		}
		
		return ks;
	}
	
	public KeyStore initialiseTrustStore(SbtsDeviceConfig sbtsConfig) throws Exception {
		if (logger.isDebugEnabled()) logger.debug("initialiseTrustStore");
		char SEP = File.separatorChar;
		File keystoreFile = new File(freak.getSbtsBase() + SEP + "certs" + SEP + "truststore.jks");
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		String password = sbtsConfig.getSettingsConfig().getTruststorePassword();
		if (keystoreFile.canRead()) {
			if (logger.isDebugEnabled()) logger.debug("Truststore (" + keystoreFile + ") exists, reading");
			FileInputStream fileInputStream = new FileInputStream(keystoreFile);
			ks.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			if (logger.isDebugEnabled()) logger.debug("Returning and ks is: " + ks);
		} else {
			if (logger.isDebugEnabled()) logger.debug("Keystore doesn't exist, creating");
			ks.load(null, null);
		}
		
		return ks;
	}
	
}
