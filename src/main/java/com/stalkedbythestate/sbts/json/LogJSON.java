package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class LogJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private String log;

	public LogJSON() {
	}
	
	public LogJSON(boolean result) {
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

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Override
	public String toString() {
		return "LogJSON [log=" + log + ", messages=" + messages + ", result="
				+ result + "]";
	}
}
