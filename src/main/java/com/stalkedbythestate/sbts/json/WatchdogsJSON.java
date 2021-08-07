package com.stalkedbythestate.sbts.json;

import java.util.*;

public class WatchdogsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private Map<String, WatchdogJSON> watchdogMap = new HashMap<String, WatchdogJSON>();
	private Collection<String> availableEventNames = new HashSet<String>();

	public WatchdogsJSON() {
	}

	public WatchdogsJSON(boolean result) {
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

	public Map<String, WatchdogJSON> getWatchdogMap() {
		return watchdogMap;
	}

	public void setWatchdogMap(Map<String, WatchdogJSON> watchdogMap) {
		this.watchdogMap = watchdogMap;
	}

	public Collection<String> getAvailableEventNames() {
		return availableEventNames;
	}

	public void setAvailableEventNames(Collection<String> availableEventNames) {
		this.availableEventNames = availableEventNames;
	}

	@Override
	public String toString() {
		return "WatchdogsJSON [availableEventNames=" + availableEventNames
				+ ", messages=" + messages + ", result=" + result
				+ ", watchdogMap=" + watchdogMap + "]";
	}
	
}
