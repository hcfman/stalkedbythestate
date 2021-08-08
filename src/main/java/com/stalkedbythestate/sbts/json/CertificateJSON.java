package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class CertificateJSON {
	private String alias;
	private Dn owner;
	private Dn issuer;
	
	private String validity;
	private String fingerprint;
	private String validFrom;
	private String validTo;
	
	private String keyAlg;
	private int keySize;
	private String serial;

	public CertificateJSON() {
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public Dn getIssuer() {
		return issuer;
	}

	public void setIssuer(Dn issuer) {
		this.issuer = issuer;
	}

	public Dn getOwner() {
		return owner;
	}

	public void setOwner(Dn owner) {
		this.owner = owner;
	}

	public String getKeyAlg() {
		return keyAlg;
	}

	public void setKeyAlg(String keyAlg) {
		this.keyAlg = keyAlg;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Override
	public String toString() {
		return "CertificateJSON [alias=" + alias + ", fingerprint="
				+ fingerprint + ", issuer=" + issuer + ", keyAlg=" + keyAlg
				+ ", keySize=" + keySize + ", owner=" + owner + ", serial="
				+ serial + ", validFrom=" + validFrom + ", validTo=" + validTo
				+ ", validity=" + validity + "]";
	}
}
