package com.stalkedbythestate.sbts.eventlib;

public abstract class AbstractEvent implements Event {
	protected EventType eventType;
	protected long eventTime;
	protected boolean guest = false;
	
	public AbstractEvent() {
		eventTime = System.currentTimeMillis();
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
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

	@Override
	public String toString() {
		return "AbstractEvent [eventTime=" + eventTime + ", eventType="
				+ eventType + ", guest=" + guest + "]";
	}
	
}
