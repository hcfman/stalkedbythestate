package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SettingsConfig {
	private volatile String version = "";
	private volatile String webPrefix = "";
	private volatile Integer encoderThreads = 1;
	private volatile Integer emailThreads = 12;
	private volatile Integer httpThreads = 12;
	private volatile Integer connectTimeout = 5000;
	private volatile boolean checkMount = true;
	private volatile Integer freeSpace = 500;
	private volatile Integer daysMJPG = 2;
	private volatile Integer cleanRate = 5; // Minutes
	private volatile String phoneHome = null;
	private volatile String ipAddress = null;
	private volatile String keystorePassword = "Change Me";
	private volatile String truststorePassword = "Change Me";
	private volatile boolean forceUpdate = false;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWebPrefix() {
		return webPrefix;
	}

	public void setWebPrefix(String webPrefix) {
		this.webPrefix = webPrefix;
	}

	public Integer getEncoderThreads() {
		return encoderThreads;
	}

	public void setEncoderThreads(Integer encoderThreads) {
		this.encoderThreads = encoderThreads;
	}

	public Integer getEmailThreads() {
		return emailThreads;
	}

	public void setEmailThreads(Integer emailThreads) {
		this.emailThreads = emailThreads;
	}

	public Integer getHttpThreads() {
		return httpThreads;
	}

	public void setHttpThreads(Integer httpThreads) {
		this.httpThreads = httpThreads;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public boolean isCheckMount() {
		return checkMount;
	}

	public void setCheckMount(boolean checkMount) {
		this.checkMount = checkMount;
	}

	public Integer getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(Integer freeSpace) {
		this.freeSpace = freeSpace;
	}

	public Integer getDaysMJPG() {
		return daysMJPG;
	}

	public void setDaysMJPG(Integer daysMJPG) {
		this.daysMJPG = daysMJPG;
	}

	public Integer getCleanRate() {
		return cleanRate;
	}

	public void setCleanRate(Integer cleanRate) {
		this.cleanRate = cleanRate;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public void addSettingsConfig(Document doc, Element parent) {
		Element settings = doc.createElement("settings");
		settings.appendChild(doc.createTextNode("\n\t"));
		
		Element webPrefixElement = doc.createElement("webPrefix");
		settings.appendChild(webPrefixElement);
		webPrefixElement.appendChild(doc.createTextNode(webPrefix));

		if (encoderThreads != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element encoderThreadsElement = doc.createElement("encoderThreads");
			settings.appendChild(encoderThreadsElement);
			encoderThreadsElement.appendChild(doc.createTextNode(encoderThreads.toString()));
		}
		
		if (emailThreads != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element emailThreadsElement = doc.createElement("emailThreads");
			settings.appendChild(emailThreadsElement);
			emailThreadsElement.appendChild(doc.createTextNode(emailThreads.toString()));
		}
		
		if (httpThreads != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element httpThreadsElement = doc.createElement("httpThreads");
			settings.appendChild(httpThreadsElement);
			httpThreadsElement.appendChild(doc.createTextNode(httpThreads.toString()));
		}
		
		if (connectTimeout != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element connectTimeoutElement = doc.createElement("connectTimeout");
			settings.appendChild(connectTimeoutElement);
			connectTimeoutElement.appendChild(doc.createTextNode(connectTimeout.toString()));
		}
		
		// checkMount
		settings.appendChild(doc.createTextNode("\n\t"));
		Element checkMountElement = doc.createElement("checkMount");
		settings.appendChild(checkMountElement);
		checkMountElement.appendChild(doc.createTextNode(Boolean.toString(checkMount)));
		
		if (freeSpace != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element freeSpaceElement = doc.createElement("freeSpace");
			settings.appendChild(freeSpaceElement);
			freeSpaceElement.appendChild(doc.createTextNode(freeSpace.toString()));
		}
		
		if (daysMJPG != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element daysMJPGElement = doc.createElement("daysMJPG");
			settings.appendChild(daysMJPGElement);
			daysMJPGElement.appendChild(doc.createTextNode(daysMJPG.toString()));
		}
		
		if (cleanRate != null) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element cleanRateElement = doc.createElement("cleanRate");
			settings.appendChild(cleanRateElement);
			cleanRateElement.appendChild(doc.createTextNode(cleanRate.toString()));
		}
		
		if (phoneHome != null && !phoneHome.trim().equals("")) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element phoneHomeElement = doc.createElement("phoneHome");
			settings.appendChild(phoneHomeElement);
			phoneHomeElement.appendChild(doc.createTextNode(phoneHome));
		}
		
		if (truststorePassword != null && !truststorePassword.trim().equals("")) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element truststorePasswordElement = doc.createElement("truststorePassword");
			settings.appendChild(truststorePasswordElement);
			truststorePasswordElement.appendChild(doc.createTextNode(truststorePassword));
		}
		
		if (keystorePassword != null && !keystorePassword.trim().equals("")) {
			settings.appendChild(doc.createTextNode("\n\t"));
			Element keystorePasswordElement = doc.createElement("keystorePassword");
			settings.appendChild(keystorePasswordElement);
			keystorePasswordElement.appendChild(doc.createTextNode(keystorePassword));
		}
		
		if (forceUpdate) {
			settings.appendChild(doc.createTextNode("\n\t"));
			settings.appendChild(doc.createElement("forceUpdate"));
		}
		
		settings.appendChild(doc.createTextNode("\n"));
		parent.appendChild(settings);
	}

}
