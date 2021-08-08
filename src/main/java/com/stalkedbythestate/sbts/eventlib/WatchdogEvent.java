package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

import java.io.Serializable;

public class WatchdogEvent extends AbstractEvent implements Nameable, Serializable {
	private static final long serialVersionUID = 8950039624244902564L;
	private String eventName;
	long eventTime;
	
	public WatchdogEvent(final String eventName, final long eventTime) {
		eventType = EventType.EVENT_WATCHDOG_TRIGGER;
		
		this.eventName = eventName;
		this.eventTime = eventTime;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(final String eventName) {
		this.eventName = eventName;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(final long eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public String toString() {
		return "WatchdogEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + "]";
	}

}
