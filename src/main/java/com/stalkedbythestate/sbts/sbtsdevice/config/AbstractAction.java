package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.*;

public abstract class AbstractAction implements Action {
	protected ActionType actionType;
	protected Set<FlagType> flags = new HashSet<FlagType>();
	protected String name;
	protected String eventName;
	protected String Description;
	protected List<TimeSpec> validTimes;
	protected int delayFor = 0;
	protected UnitType delayUnits;
	protected int hysteresis = 0;
	protected UnitType hysteresisUnits = UnitType.sec;
	protected SortedSet<String> profiles = new TreeSet<String>();
	protected SortedSet<String> positiveTagNames = new TreeSet<String>();
	protected boolean positiveTagAndMode = false;
	protected SortedSet<String> negativeTagNames = new TreeSet<String>();
	protected EventCounter eventcounter = null;
	protected boolean guest;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public List<TimeSpec> getValidTimes() {
		return validTimes;
	}

	public void setValidTimes(List<TimeSpec> validTimes) {
		this.validTimes = validTimes;
	}

	public SortedSet<String> getPositiveTagNames() {
		return positiveTagNames;
	}

	public void setPositiveTagNames(SortedSet<String> positiveTagNames) {
		this.positiveTagNames = positiveTagNames;
	}

	public SortedSet<String> getNegativeTagNames() {
		return negativeTagNames;
	}

	public void setNegativeTagNames(SortedSet<String> negativeTagNames) {
		this.negativeTagNames = negativeTagNames;
	}

	public SortedSet<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(SortedSet<String> profiles) {
		this.profiles = profiles;
	}

	public Set<FlagType> getFlags() {
		return flags;
	}

	public void setFlag(FlagType flag) {
		this.flags.add(flag);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDelayFor() {
		return delayFor;
	}

	public void setDelayFor(int delayFor) {
		this.delayFor = delayFor;
	}

	public UnitType getDelayUnits() {
		return delayUnits;
	}

	public void setDelayUnits(UnitType delayUnits) {
		this.delayUnits = delayUnits;
	}

	public int getHysteresis() {
		return hysteresis;
	}

	public void setHysteresis(int hysteresis) {
		this.hysteresis = hysteresis;
	}

	public UnitType getHysteresisUnits() {
		return hysteresisUnits;
	}

	public void setHysteresisUnits(UnitType hysteresisUnits) {
		this.hysteresisUnits = hysteresisUnits;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public EventCounter getEventcounter() {
		return eventcounter;
	}

	public void setEventcounter(EventCounter eventcounter) {
		this.eventcounter = eventcounter;
	}

	public boolean canTrigger() {
		if ( validTimes == null )
			return true;
		
		long now = System.currentTimeMillis();
		
		for ( TimeSpec t : validTimes )
			if ( t.within(now))
				return true;
		
		return false;		
	}

	public boolean isPositiveTagAndMode() {
		return positiveTagAndMode;
	}

	public void setPositiveTagAndMode(boolean positiveTagAndMode) {
		this.positiveTagAndMode = positiveTagAndMode;
	}
	
}
