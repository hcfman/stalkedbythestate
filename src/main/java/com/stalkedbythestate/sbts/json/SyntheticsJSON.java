package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.*;

public class SyntheticsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private Map<String, SyntheticJSON> syntheticMap = new HashMap<String, SyntheticJSON>();
	private Collection<String> availableEventNames = new HashSet<String>();

	public SyntheticsJSON() {
	}

	public SyntheticsJSON(boolean result) {
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

	public Map<String, SyntheticJSON> getSyntheticMap() {
		return syntheticMap;
	}

	public void setSyntheticMap(Map<String, SyntheticJSON> syntheticMap) {
		this.syntheticMap = syntheticMap;
	}

	public Collection<String> getAvailableEventNames() {
		return availableEventNames;
	}

	public void setAvailableEventNames(Collection<String> availableEventNames) {
		this.availableEventNames = availableEventNames;
	}

	@Override
	public String toString() {
		return "SyntheticsJSON [availableEventNames=" + availableEventNames
				+ ", messages=" + messages + ", result=" + result
				+ ", syntheticMap=" + syntheticMap + "]";
	}
	
}
