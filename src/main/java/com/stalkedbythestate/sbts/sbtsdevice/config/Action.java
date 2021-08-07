package com.stalkedbythestate.sbts.sbtsdevice.config;

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface Action {
	public String getName();
	
	public String getEventName();

	public void setEventName(String name);

	public String getDescription();

	public void setDescription(String description);

	public ActionType getActionType();

	public void setActionType(ActionType actionType);

	public List<TimeSpec> getValidTimes();

	public void setValidTimes(List<TimeSpec> validTimes);
	
	public SortedSet<String> getPositiveTagNames();

	public void setPositiveTagNames(SortedSet<String> positiveTagNames);

	public SortedSet<String> getNegativeTagNames();

	public void setNegativeTagNames(SortedSet<String> negativeTagNames);
	
	public SortedSet<String> getProfiles();
	
	public void setProfiles(SortedSet<String> profiles);

	public int getDelayFor();

	public void setDelayFor(int delayFor);
	
	public UnitType getDelayUnits();

	public void setDelayUnits(UnitType delayUnits);
	
	public boolean canTrigger();
	
	public Set<FlagType> getFlags();

	public void setFlag(FlagType flag);

	public int getHysteresis();

	public void setHysteresis(int hysteresis);
	
	public UnitType getHysteresisUnits();

	public void setHysteresisUnits(UnitType hysteresisUnits);
	
	public boolean isGuest();
	
	public void setGuest(boolean guest);
	
	public EventCounter getEventcounter();

	public void setEventcounter(EventCounter eventcounter);
	
	public boolean isPositiveTagAndMode();

	public void setPositiveTagAndMode(boolean positiveTagAndMode);
}
