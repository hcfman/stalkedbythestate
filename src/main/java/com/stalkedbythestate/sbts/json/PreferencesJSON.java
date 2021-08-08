package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class PreferencesJSON {
	private String webPrefix;
	private int connectTimeout;
	private int freeSpace;
	private int daysJpeg;
	private int cleanRate;
	private String phonehomeUrl;

	public PreferencesJSON() {
	}

	public String getWebPrefix() {
		return webPrefix;
	}

	public void setWebPrefix(String webPrefix) {
		this.webPrefix = webPrefix;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(int freeSpace) {
		this.freeSpace = freeSpace;
	}

	public int getCleanRate() {
		return cleanRate;
	}

	public int getDaysJpeg() {
		return daysJpeg;
	}

	public void setDaysJpeg(int daysJpeg) {
		this.daysJpeg = daysJpeg;
	}

	public void setCleanRate(int cleanRate) {
		this.cleanRate = cleanRate;
	}

	public String getPhonehomeUrl() {
		return phonehomeUrl;
	}

	public void setPhonehomeUrl(String phonehomeUrl) {
		this.phonehomeUrl = phonehomeUrl;
	}

	@Override
	public String toString() {
		return "PreferencesJSON [cleanRate=" + cleanRate + ", connectTimeout="
				+ connectTimeout + ", daysJpeg=" + daysJpeg + ", freeSpace="
				+ freeSpace + ", phonehomeUrl=" + phonehomeUrl + ", webPrefix="
				+ webPrefix + "]";
	}

}
