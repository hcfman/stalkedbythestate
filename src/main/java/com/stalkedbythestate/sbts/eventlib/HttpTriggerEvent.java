package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class HttpTriggerEvent extends AbstractEvent implements Nameable,
		ContainsClientEventTime {
	private String eventName;
	private long eventTime;
	private long clientEventTime;
	private boolean guest = false;

	public HttpTriggerEvent(String eventName, long eventTime,
                            long clientEventTime, boolean guest) {
		eventType = EventType.EVENT_HTTP_TRIGGER;

		this.eventName = eventName;
		this.eventTime = eventTime;
		this.clientEventTime = clientEventTime;
		this.guest = guest;
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

	public long getClientEventTime() {
		return clientEventTime;
	}

	public void setClientEventTime(long clientEventTime) {
		this.clientEventTime = clientEventTime;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	@Override
	public String toString() {
		return "HttpTriggerEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + ", clientEventTime=" + clientEventTime
				+ ", guest=" + guest + "]";
	}

}
