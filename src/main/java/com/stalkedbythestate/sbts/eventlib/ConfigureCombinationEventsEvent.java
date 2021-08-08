package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class ConfigureCombinationEventsEvent extends AbstractEvent {
	
	public ConfigureCombinationEventsEvent() {
		eventType = EventType.EVENT_CONFIGURE_COMBINATION_EVENTS;
	}

	@Override
	public String toString() {
		return "ConfigureCombinationEventsEvent []";
	}

}
