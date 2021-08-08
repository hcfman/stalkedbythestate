package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.TriggerGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TriggerGroupImpl implements TriggerGroup{
	private String name;
	// The triggers in this group cause this trigger to trigger
	private Collection<Action> group = new HashSet<Action>();
	// This is the list of result triggers
	private List<Action> triggers = new ArrayList<Action>();
	long withinTime;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Collection<Action> getGroup() {
		return group;
	}

	public void setGroup(Collection<Action> group) {
		this.group = group;
	}

	public List<Action> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<Action> triggers) {
		this.triggers = triggers;
	}
	
	public long getWithinTime() {
		return withinTime;
	}

	public void setWithinTime(long withinTime) {
		this.withinTime = withinTime;
	}
}
