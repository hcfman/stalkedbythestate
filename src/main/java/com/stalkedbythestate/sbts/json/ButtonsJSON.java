package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private Map<String, List<ButtonJSON>> buttonGroups = new HashMap<String, List<ButtonJSON>>();

	public ButtonsJSON() {
	}

	public ButtonsJSON(boolean result) {
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

	public Map<String, List<ButtonJSON>> getButtonGroups() {
		return buttonGroups;
	}

	public void setButtonGroups(Map<String, List<ButtonJSON>> buttonGroups) {
		this.buttonGroups = buttonGroups;
	}

	@Override
	public String toString() {
		return "ButtonsJSON [buttonGroups=" + buttonGroups + ", messages="
				+ messages + ", result=" + result + "]";
	}
	
}
