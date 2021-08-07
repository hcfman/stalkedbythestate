package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class CertificatesJSON {
	private CertificateChainJSON certificate;
	private List<CertificateJSON> keystore = new ArrayList<CertificateJSON>();
	private List<CertificateJSON> truststore = new ArrayList<CertificateJSON>();
	private List<String> disallowedAliases = new ArrayList<String>();

	private boolean result = true;
	private List<String> messages = new ArrayList<String>();

	public CertificatesJSON() {
		disallowedAliases.add("sbts");
	}

	public CertificatesJSON(boolean result) {
		this.result = result;

		disallowedAliases.add("sbts");
	}

	public CertificateChainJSON getCertificate() {
		return certificate;
	}

	public void setCertificate(CertificateChainJSON certificate) {
		this.certificate = certificate;
	}

	public List<String> getDisallowedAliases() {
		return disallowedAliases;
	}

	public void setDisallowedAliases(List<String> disallowedAliases) {
		this.disallowedAliases = disallowedAliases;
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

	public List<CertificateJSON> getKeystore() {
		return keystore;
	}

	public void setKeystore(List<CertificateJSON> keystore) {
		this.keystore = keystore;
	}

	public List<CertificateJSON> getTruststore() {
		return truststore;
	}

	public void setTruststore(List<CertificateJSON> truststore) {
		this.truststore = truststore;
	}

	@Override
	public String toString() {
		return "CertificatesJSON [certificate=" + certificate
				+ ", disallowedAliases=" + disallowedAliases + ", keystore="
				+ keystore + ", messages=" + messages + ", result=" + result
				+ ", truststore=" + truststore + "]";
	}
}
