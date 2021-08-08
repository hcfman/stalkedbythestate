package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemStatusJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private boolean diskUp = false;
	private String diskMessage;
	private boolean rfxcomStatus = false;
	private Map<Integer, Boolean> phidgetStatus = new HashMap<Integer, Boolean>();
	private List<CameraStatusJSON> cameraStatus = new ArrayList<CameraStatusJSON>();

	public SystemStatusJSON() {
	}

	public SystemStatusJSON(boolean result) {
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

	public boolean isRfxcomStatus() {
		return rfxcomStatus;
	}

	public void setRfxcomStatus(boolean rfxcomStatus) {
		this.rfxcomStatus = rfxcomStatus;
	}

	public Map<Integer, Boolean> getPhidgetStatus() {
		return phidgetStatus;
	}

	public void setPhidgetStatus(Map<Integer, Boolean> phidgetStatus) {
		this.phidgetStatus = phidgetStatus;
	}

	public List<CameraStatusJSON> getCameraStatus() {
		return cameraStatus;
	}

	public void setCameraStatus(List<CameraStatusJSON> cameraStatus) {
		this.cameraStatus = cameraStatus;
	}

	public boolean isDiskUp() {
		return diskUp;
	}

	public void setDiskUp(boolean diskUp) {
		this.diskUp = diskUp;
	}

	public String getDiskMessage() {
		return diskMessage;
	}

	public void setDiskMessage(String diskMessage) {
		this.diskMessage = diskMessage;
	}

	@Override
	public String toString() {
		return "SystemStatusJSON ["
				+ ", cameraStatus=" + cameraStatus + ", diskMessage="
				+ diskMessage + ", diskUp=" + diskUp + ", messages=" + messages
				+ ", phidgetStatus=" + phidgetStatus + ", result=" + result
				+ "]";
	}
}
