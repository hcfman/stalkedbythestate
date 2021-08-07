package com.stalkedbythestate.sbts.eventlib;

import com.stalkedbythestate.sbts.sbtsdevice.config.Action;

public class SendActionEvent extends AbstractEvent {
	private Action action;
	private Event originalEvent;
	
	public SendActionEvent(Action action, Event originalEvent, long eventTime) {
		eventType = EventType.EVENT_ACTION;
		
		this.action = action;
		this.originalEvent = originalEvent;
		this.eventTime = eventTime;
	}
	
	public Action getAction() {
		return action;
	}


	public void setAction(Action action) {
		this.action = action;
	}

	public Event getOriginalEvent() {
		return originalEvent;
	}

	public void setOriginalEvent(Event originalEvent) {
		this.originalEvent = originalEvent;
	}

	@Override
	public String toString() {
		return "SendActionEvent [action=" + action + ", originalEvent="
				+ originalEvent + ", eventTime=" + eventTime + ", eventType="
				+ eventType + "]";
	}
	
}
