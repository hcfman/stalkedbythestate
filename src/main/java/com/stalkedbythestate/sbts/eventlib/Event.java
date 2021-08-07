package com.stalkedbythestate.sbts.eventlib;

public interface Event {
	public EventType getEventType();
	public long getEventTime();
	public boolean isGuest();
}
