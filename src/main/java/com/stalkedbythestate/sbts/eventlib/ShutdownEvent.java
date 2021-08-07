package com.stalkedbythestate.sbts.eventlib;

public class ShutdownEvent extends AbstractEvent {

	public ShutdownEvent() {
		eventType = EventType.EVENT_SHUTDOWN;
	}

}
