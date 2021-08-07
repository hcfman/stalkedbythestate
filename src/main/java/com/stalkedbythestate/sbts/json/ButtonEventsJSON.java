package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class ButtonEventsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<String> eventNames = new ArrayList<String>();

	public ButtonEventsJSON() {
	}

	public ButtonEventsJSON(boolean result) {
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

	public List<String> getEventNames() {
		return eventNames;
	}

	public void setEventNames(List<String> eventNames) {
		this.eventNames = eventNames;
	}

	@Override
	public String toString() {
		return "ButtonsJSON [eventNames=" + eventNames + ", messages="
				+ messages + ", result=" + result + "]";
	}
	
}
