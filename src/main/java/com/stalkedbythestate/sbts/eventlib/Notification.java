package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

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
