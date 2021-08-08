package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class ResultMessage {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();

	public ResultMessage() {
	}

	public ResultMessage(boolean result, String message) {
		this.result = result;
		messages.add(message);
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
		return "ResultMessage [messages=" + messages + ", result=" + result
				+ "]";
	}
	
}
