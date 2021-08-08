package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class CertificateChainJSON {
	private String entryType;
	private String alias;
	private List<CertificateJSON> certificateChain = new ArrayList<CertificateJSON>();

	public CertificateChainJSON() {
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<CertificateJSON> getCertificateChain() {
		return certificateChain;
	}

	public void setCertificateChain(List<CertificateJSON> certificateChain) {
		this.certificateChain = certificateChain;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	@Override
	public String toString() {
		return "CertificateChainJSON [alias=" + alias + ", certificateChain="
				+ certificateChain + ", entryType=" + entryType + "]";
	}
}
