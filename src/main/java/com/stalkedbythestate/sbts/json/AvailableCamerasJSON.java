package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class AvailableCamerasJSON {
	private boolean result = true;
	private List<String> messages = new ArrayList<String>();
	private List<String> availableCameras;

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

	public List<String> getAvailableCameras() {
		return availableCameras;
	}

	public void setAvailableCameras(List<String> availableCameras) {
		this.availableCameras = availableCameras;
	}

	@Override
	public String toString() {
		return "AvailableCamerasJSON [availableCameras=" + availableCameras
				+ ", messages=" + messages + ", result=" + result + "]";
	}


}
