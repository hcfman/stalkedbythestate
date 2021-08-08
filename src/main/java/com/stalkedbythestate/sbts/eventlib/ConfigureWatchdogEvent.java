package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class ConfigureWatchdogEvent extends AbstractEvent {
	
	public ConfigureWatchdogEvent() {
		eventType = EventType.EVENT_CONFIGURE;
	}

	@Override
	public String toString() {
		return "ConfigureWatchdogEvent []";
	}

}
