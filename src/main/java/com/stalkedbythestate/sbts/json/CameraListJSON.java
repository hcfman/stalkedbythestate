package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CameraListJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private SortedSet<Integer> cameraList = new TreeSet<Integer>();

	public CameraListJSON() {
	}

	public CameraListJSON(boolean result) {
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

	public SortedSet<Integer> getCameraList() {
		return cameraList;
	}

	public void setCameraList(SortedSet<Integer> cameraList) {
		this.cameraList = cameraList;
	}

	@Override
	public String toString() {
		return "CameraListJSON [cameraList=" + cameraList + ", messages="
				+ messages + ", result=" + result + "]";
	}
	
}
