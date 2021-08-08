package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class RfxcomsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<RfxcomCommandJSON> commandList = new ArrayList<RfxcomCommandJSON>();

	public RfxcomsJSON() {
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

	public List<RfxcomCommandJSON> getCommandList() {
		return commandList;
	}

	public void setCommandList(List<RfxcomCommandJSON> commandList) {
		this.commandList = commandList;
	}

	@Override
	public String toString() {
		return "RfxcomsJSON [result=" + result + ", messages=" + messages
				+ ", commandList=" + commandList + "]";
	}

}
