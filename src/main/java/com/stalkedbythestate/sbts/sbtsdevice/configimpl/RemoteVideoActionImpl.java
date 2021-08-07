package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;

import java.util.SortedSet;

public class RemoteVideoActionImpl extends VideoActionImpl {
	private String freakName;
	
	public RemoteVideoActionImpl(String name, String eventName, String description, SortedSet<Integer> cameraList, String freakName) {
		super(name, eventName, description, cameraList);
		setActionType(ActionType.ACTION_REMOTE_VIDEO);
		this.freakName = freakName;
	}

	public String getFreakName() {
		return freakName;
	}

	public void setFreakName(String freakName) {
		this.freakName = freakName;
	}

	@Override
	public String toString() {
		return "RemoteVideoActionImpl [freakName=" + freakName + "]";
	}
	
}
