package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.Profile;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.List;

public class ProfileImpl implements Profile, Comparable<Profile> {
	String name;
	String tagName;
	String description;
	List<TimeSpec> validTimes;
	Boolean isOn;
	
	public ProfileImpl(String name, String tagName, String description) {
		this.name = name;
		this.tagName = tagName;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(Profile o) {
		return tagName.compareTo(o.getTagName());
	}

	public List<TimeSpec> getValidTimes() {
		return validTimes;
	}

	public void setValidTimes(List<TimeSpec> validTimes) {
		this.validTimes = validTimes;
	}

	public Boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(Boolean isOn) {
		this.isOn = isOn;
	}

	@Override
	public String toString() {
		return "ProfileImpl [description=" + description + ", isOn=" + isOn
				+ ", name=" + name + ", tagName=" + tagName + ", validTimes="
				+ validTimes + "]";
	}
	
}
