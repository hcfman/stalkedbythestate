package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.List;

public class CamerasJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<CameraJSON> cameras = new ArrayList<CameraJSON>();

	public CamerasJSON() {
	}

	public CamerasJSON(boolean result) {
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

	public List<CameraJSON> getCameras() {
		return cameras;
	}

	public void setCameras(List<CameraJSON> cameras) {
		this.cameras = cameras;
	}

	@Override
	public String toString() {
		return "CamerasJSON [cameras=" + cameras + ", messages=" + messages
				+ ", result=" + result + "]";
	}
	
}
