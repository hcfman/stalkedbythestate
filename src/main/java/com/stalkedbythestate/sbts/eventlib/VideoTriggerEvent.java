package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class VideoTriggerEvent extends AbstractEvent {
	private int cameraIndex;
	private String description;
	
	public VideoTriggerEvent(int cameraIndex, String description, long eventTime) {
		eventType = EventType.VIDEO_TRIGGER;
		
		this.cameraIndex = cameraIndex;
		this.description = description;
		this.eventTime = eventTime;
	}

	public int getCameraIndex() {
		return cameraIndex;
	}

	public void setCameraIndex(int cameraIndex) {
		this.cameraIndex = cameraIndex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Type: " + getEventType());
		sb.append(", camera index: " + cameraIndex);
		
		if (description != null)
			sb.append(", description: " + description);
		
		return sb.toString();
	}

}
