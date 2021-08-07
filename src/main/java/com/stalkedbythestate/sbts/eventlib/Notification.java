package com.stalkedbythestate.sbts.eventlib;

public class Notification {
	private boolean closing;
	private Nameable event;

	public Notification(boolean closing) {
		this.closing = closing;
	}

	public Notification(Nameable event) {
		this.event = event;
	}

	public boolean isClosing() {
		return closing;
	}

	public Nameable getEvent() {
		return event;
	}

}
