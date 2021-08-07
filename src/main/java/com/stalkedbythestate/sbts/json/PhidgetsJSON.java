package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhidgetsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private Map<String, PhidgetJSON> phidgetMap = new HashMap<String, PhidgetJSON>();

	public PhidgetsJSON() {
	}

	public PhidgetsJSON(boolean result) {
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

	public Map<String, PhidgetJSON> getPhidgetMap() {
		return phidgetMap;
	}

	public void setPhidgetMap(Map<String, PhidgetJSON> phidgetMap) {
		this.phidgetMap = phidgetMap;
	}

	@Override
	public String toString() {
		return "PhidgetsJSON [messages=" + messages + ", phidgetMap="
				+ phidgetMap + ", result=" + result + "]";
	}
	
}
