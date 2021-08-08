package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class RfxcomTriggerEvent extends AbstractEvent implements Nameable, ContainsPacket {
	private String eventName;
	private long eventTime;
	private String stringRepresentation;
	
	
	public RfxcomTriggerEvent(String eventName, long eventTime, String stringRepresentation) {
		eventType = EventType.EVENT_RFXCOM_TRIGGER;
		
		this.eventName = eventName;
		this.eventTime = eventTime;
		this.stringRepresentation = stringRepresentation;
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
		return "RfxcomTriggerEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + "]";
	}

	@Override
	public String getPacketString() {
		return stringRepresentation;
	}

}
