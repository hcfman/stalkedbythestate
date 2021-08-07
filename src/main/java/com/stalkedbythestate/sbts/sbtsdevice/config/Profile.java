package com.stalkedbythestate.sbts.sbtsdevice.config;

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.List;

public interface Profile extends Comparable<Profile> {
	public String getName();

	public void setName(String name);
	
	public String getTagName();

	public void setTagName(String tagame);

	public String getDescription();

	public void setDescription(String description);
	
	public List<TimeSpec> getValidTimes();

	public void setValidTimes(List<TimeSpec> validTimes);

	public Boolean getIsOn();

	public void setIsOn(Boolean isOn);
}
