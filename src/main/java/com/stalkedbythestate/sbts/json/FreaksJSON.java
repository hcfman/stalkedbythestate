package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class FreaksJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<FreakJSON> freaks = new ArrayList<FreakJSON>();

	public FreaksJSON() {
	}

	public FreaksJSON(boolean result) {
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

	public List<FreakJSON> getFreaks() {
		return freaks;
	}

	public void setFreaks(List<FreakJSON> freaks) {
		this.freaks = freaks;
	}

	@Override
	public String toString() {
		return "FreaksJSON [freaks=" + freaks + ", messages=" + messages
				+ ", result=" + result + "]";
	}
	
}
