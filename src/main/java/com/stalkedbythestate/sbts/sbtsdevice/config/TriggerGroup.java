package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.util.Collection;
import java.util.List;

public interface TriggerGroup {
	public String getName();
	
	public void setName(String name);

	public Collection<Action> getGroup();

	public void setGroup(Collection<Action> group);

	public List<Action> getTriggers();

	public void setTriggers(List<Action> triggers);
	
	public long getWithinTime();

	public void setWithinTime(long withinTime);
}
