package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class ExportJSON {
	boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private String csr;

	public ExportJSON() {
	}

	public String getCsr() {
		return csr;
	}

	public void setCsr(String csr) {
		this.csr = csr;
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
		return "ExportJSON [csr=" + csr + ", messages=" + messages
				+ ", result=" + result + "]";
	}
	
}
