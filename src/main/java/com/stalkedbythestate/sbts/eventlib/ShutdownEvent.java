package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class ShutdownEvent extends AbstractEvent {

	public ShutdownEvent() {
		eventType = EventType.EVENT_SHUTDOWN;
	}

}
