package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class PhidgetTriggerEvent extends AbstractEvent implements Nameable {
	private String eventName;
	long eventTime;
	
	public PhidgetTriggerEvent(String eventName, long eventTime) {
		eventType = EventType.EVENT_PHIDGET_IO_TRIGGER;
		
		this.eventName = eventName;
		this.eventTime = eventTime;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public long getEventTime() {
		return eventTime;
	}

	@Override
	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public String toString() {
		return "PhidgetTriggerEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + "]";
	}

}
