package com.stalkedbythestate.sbts.json;

import java.util.List;

public class SystemSettingsJSON {
	private DateTimeJSON dateTime;
	private NetworkJSON network;
	private PreferencesJSON preferences;
	
	private boolean result = false;
	private List<String> messages;
	
	public SystemSettingsJSON() {
	}

	public DateTimeJSON getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTimeJSON dateTime) {
		this.dateTime = dateTime;
	}

	public NetworkJSON getNetwork() {
		return network;
	}

	public void setNetwork(NetworkJSON network) {
		this.network = network;
	}

	public PreferencesJSON getPreferences() {
		return preferences;
	}

	public void setPreferences(PreferencesJSON preferences) {
		this.preferences = preferences;
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
		return "SystemSettingsJSON [dateTime=" + dateTime + ", messages="
				+ messages + ", network=" + network + ", preferences="
				+ preferences + ", result=" + result + "]";
	}

}
