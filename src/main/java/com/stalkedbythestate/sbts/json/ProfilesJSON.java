package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class ProfilesJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<ProfileJSON> profilesJSON = new ArrayList<ProfileJSON>();

	public ProfilesJSON() {
	}

	public ProfilesJSON(boolean result) {
		this.result = result;
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

	public List<ProfileJSON> getProfilesJSON() {
		return profilesJSON;
	}

	public void setProfilesJSON(List<ProfileJSON> profilesJSON) {
		this.profilesJSON = profilesJSON;
	}

	@Override
	public String toString() {
		return "ProfilesJSON [messages=" + messages + ", profilesJSON="
				+ profilesJSON + ", result=" + result + "]";
	}
	
}
