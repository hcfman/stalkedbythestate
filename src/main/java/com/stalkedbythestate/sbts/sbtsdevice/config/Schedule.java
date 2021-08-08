package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

public class Schedule {
	private String name = "";
	private TimeSpec timeSpec;
	private String eventName;

	public Schedule() {
	}

	public Schedule(String name, TimeSpec timeSpec, String eventName) {
		this.name = name;
		this.timeSpec = timeSpec;
		this.eventName = eventName;
	}

	public TimeSpec getTimeSpec() {
		return timeSpec;
	}

	public void setTimeSpec(TimeSpec timeSpec) {
		this.timeSpec = timeSpec;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Schedule [eventName=" + eventName + ", name=" + name
				+ ", timeSpec=" + timeSpec + "]";
	}
	
}
