package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class ScheduleTriggerEvent extends AbstractEvent implements Nameable {
	private String eventName;
	long eventTime;
	
	public ScheduleTriggerEvent(String eventName, long eventTime) {
		eventType = EventType.EVENT_SCHEDULE_TRIGGER;
		
		this.eventName = eventName;
		this.eventTime = eventTime;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public String toString() {
		return "ScheduleTriggerEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + "]";
	}

}
