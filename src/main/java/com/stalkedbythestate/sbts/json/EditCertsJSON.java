package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EditCertsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private CertificateJSON[] keystore;
	private CertificateJSON[] truststore;

	public EditCertsJSON() {
	}

	public CertificateJSON[] getKeystore() {
		return keystore;
	}

	public void setKeystore(CertificateJSON[] keystore) {
		this.keystore = keystore;
	}

	public CertificateJSON[] getTruststore() {
		return truststore;
	}

	public void setTruststore(CertificateJSON[] truststore) {
		this.truststore = truststore;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "EditCertsJSON [keystore=" + Arrays.toString(keystore)
				+ ", messages=" + messages + ", result=" + result
				+ ", truststore=" + Arrays.toString(truststore) + "]";
	}

}
