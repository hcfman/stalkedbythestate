package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateCheckJSON {
	private boolean result;
	private List<String> messages = new ArrayList<String>();
	private String version;
	private int size;
	private String[] description;
	private int parts;

	public UpdateCheckJSON() {
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public int getParts() {
		return parts;
	}

	public void setParts(int parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		return "UpdateCheckJSON [result=" + result + ", messages=" + messages
				+ ", version=" + version + ", size=" + size + ", description="
				+ Arrays.toString(description) + ", parts=" + parts + "]";
	}

}
