package com.stalkedbythestate.sbts.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dn {
	private String commonName;
	private String organisation;
	private String organisationalUnit;
	private String state;
	private String locality;
	private String country;

	public Dn() {
	}

	public Dn(String commonName, String organisation,
              String organisationalUnit, String state, String locality,
              String country) {
		this.commonName = commonName;
		this.organisation = organisation;
		this.organisationalUnit = organisationalUnit;
		this.state = state;
		this.locality = locality;
		this.country = country;
	}

	public static Dn parseDn(String dnString) {
		Dn dn = new Dn();

		Pattern cnPattern = Pattern.compile("(?i)CN=([^,]+)");
		Matcher cnMatcher = cnPattern.matcher(dnString);
		String commonName = null;
		if (cnMatcher.find()) {
			commonName = cnMatcher.group(1);
			dn.setCommonName(commonName);
		}

		Pattern ouPattern = Pattern.compile("(?i)OU=([^,]+)");
		Matcher ouMatcher = ouPattern.matcher(dnString);
		String organisationalUnit = null;
		if (ouMatcher.find()) {
			organisationalUnit = ouMatcher.group(1);
			dn.setOrganisationalUnit(organisationalUnit);
		}

		Pattern oPattern = Pattern.compile("(?i)O=([^,]+)");
		Matcher oMatcher = oPattern.matcher(dnString);
		String organisation = null;
		if (oMatcher.find()) {
			organisation = oMatcher.group(1);
			dn.setOrganisation(organisation);
		}

		Pattern lPattern = Pattern.compile("(?i)L=([^,]+)");
		Matcher lMatcher = lPattern.matcher(dnString);
		String locality = null;
		if (lMatcher.find()) {
			locality = lMatcher.group(1);
			dn.setLocality(locality);
		}

		Pattern stPattern = Pattern.compile("(?i)ST=([^,]+)");
		Matcher stMatcher = stPattern.matcher(dnString);
		String state = null;
		if (stMatcher.find()) {
			state = stMatcher.group(1);
			dn.setState(state);
		}

		Pattern cPattern = Pattern.compile("(?i)C=([^,]+)");
		Matcher cMatcher = cPattern.matcher(dnString);
		String country = null;
		if (cMatcher.find()) {
			country = cMatcher.group(1);
			dn.setCountry(country);
		}

		return dn;
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

	@Override
	public String toString() {
		return "Dn [commonName=" + commonName + ", country=" + country
				+ ", locality=" + locality + ", organisation=" + organisation
				+ ", organisationalUnit=" + organisationalUnit + ", state="
				+ state + "]";
	}
}
