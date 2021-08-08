package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public class HttpTrigger {
	private String eventName;
	private String description;
	private boolean guest = false;

	public HttpTrigger(String eventName, String description, boolean isGuest) {
		this.eventName = eventName;
		this.description = description;
		this.guest = isGuest;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	@Override
	public String toString() {
		return "HttpTrigger [description=" + description + ", eventName="
				+ eventName + ", guest=" + guest + "]";
	}

}
