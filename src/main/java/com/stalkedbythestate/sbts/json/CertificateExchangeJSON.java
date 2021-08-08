package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;


public class CertificateExchangeJSON {
	private String commonName;
	private String locality;
	private String state;
	private String organisationalUnit;
	private String organisation;
	private String country;
	private String validity;
	

	private boolean result = true;
	private List<String> messages = new ArrayList<String>();

	public CertificateExchangeJSON() {
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrganisationalUnit() {
		return organisationalUnit;
	}

	public void setOrganisationalUnit(String organisationalUnit) {
		this.organisationalUnit = organisationalUnit;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
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
		return "CertificateExchangeJSON [commonName=" + commonName
				+ ", country=" + country + ", locality=" + locality
				+ ", messages=" + messages + ", organisation=" + organisation
				+ ", organisationalUnit=" + organisationalUnit + ", result="
				+ result + ", state=" + state + ", validity=" + validity + "]";
	}
}
