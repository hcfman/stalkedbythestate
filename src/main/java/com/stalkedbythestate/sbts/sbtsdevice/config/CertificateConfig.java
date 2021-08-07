package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.security.KeyStore;

public class CertificateConfig {
	private volatile KeyStore keyStore;
	private volatile KeyStore trustStore;

	public KeyStore getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public KeyStore getTrustStore() {
		return trustStore;
	}

	public void setTrustStore(KeyStore trustStore) {
		this.trustStore = trustStore;
	}

	@Override
	public String toString() {
		return "CertificateConfig [keyStore=" + keyStore + ", trustStore=" + trustStore + "]";
	}

}
