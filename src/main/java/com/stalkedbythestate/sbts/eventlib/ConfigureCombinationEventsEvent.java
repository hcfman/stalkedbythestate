package com.stalkedbythestate.sbts.eventlib;

public class ConfigureCombinationEventsEvent extends AbstractEvent {
	
	public ConfigureCombinationEventsEvent() {
		eventType = EventType.EVENT_CONFIGURE_COMBINATION_EVENTS;
	}

	@Override
	public String toString() {
		return "ConfigureCombinationEventsEvent []";
	}

}
