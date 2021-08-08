package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

import java.util.SortedSet;

public class RemoteCamTriggerEvent extends AbstractEvent implements Nameable {
	private String eventName;
	private String description;
	private long eventTime;
	private boolean guest = false;
	private SortedSet<Integer> cameraSet;
	
	public RemoteCamTriggerEvent(String eventName, String description, long eventTime, boolean guest, SortedSet<Integer> cameraSet) {
		eventType = EventType.EVENT_REMOTE_CAM_TRIGGER;
		
		this.eventName = eventName;
		this.description = description;
		this.eventTime = eventTime;
		this.guest = guest;
		this.cameraSet = cameraSet;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public SortedSet<Integer> getCameraSet() {
		return cameraSet;
	}

	public void setCameraSet(SortedSet<Integer> cameraSet) {
		this.cameraSet = cameraSet;
	}

	@Override
	public String toString() {
		return "RemoteCamTriggerEvent [cameraSet=" + cameraSet
				+ ", description=" + description + ", eventName=" + eventName
				+ ", eventTime=" + eventTime + ", guest=" + guest + "]";
	}

}
