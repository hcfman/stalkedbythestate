package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public interface Event {
	public EventType getEventType();
	public long getEventTime();
	public boolean isGuest();
}
