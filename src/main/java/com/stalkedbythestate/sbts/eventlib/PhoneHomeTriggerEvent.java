package com.stalkedbythestate.sbts.eventlib;

public class PhoneHomeTriggerEvent extends AbstractEvent implements Nameable {
	private String eventName;
	long eventTime;
	
	public PhoneHomeTriggerEvent(String eventName, long eventTime) {
		eventType = EventType.EVENT_PHONE_HOME;
		
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
		return "PhoneHomeTriggerEvent [eventName=" + eventName + ", eventTime="
				+ eventTime + "]";
	}

}
