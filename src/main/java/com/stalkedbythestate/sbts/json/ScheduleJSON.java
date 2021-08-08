package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.Schedule;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;


public class ScheduleJSON {
	private String name;
	private String eventName;
	private TimeSpec timeSpec;

	public ScheduleJSON() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public TimeSpec getTimeSpec() {
		return timeSpec;
	}

	public void setTimeSpec(TimeSpec timeSpec) {
		this.timeSpec = timeSpec;
	}
	
	public Schedule toSchedule() {
		return new Schedule(name, timeSpec, eventName);
	}

	@Override
	public String toString() {
		return "ScheduleJSON [eventName=" + eventName + ", name=" + name
				+ ", timeSpec=" + timeSpec + "]";
	}

}
