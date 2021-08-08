package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class SchedulesJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<ScheduleJSON> schedules = new ArrayList<ScheduleJSON>();

	public SchedulesJSON() {
	}

	public SchedulesJSON(boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<ScheduleJSON> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleJSON> schedules) {
		this.schedules = schedules;
	}

	@Override
	public String toString() {
		return "SchedulesJSON [messages=" + messages + ", result=" + result
				+ ", schedules=" + schedules + "]";
	}

}
