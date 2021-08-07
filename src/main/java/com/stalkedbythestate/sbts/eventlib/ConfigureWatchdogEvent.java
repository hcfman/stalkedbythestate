package com.stalkedbythestate.sbts.eventlib;

public class ConfigureWatchdogEvent extends AbstractEvent {
	
	public ConfigureWatchdogEvent() {
		eventType = EventType.EVENT_CONFIGURE;
	}

	@Override
	public String toString() {
		return "ConfigureWatchdogEvent []";
	}

}
