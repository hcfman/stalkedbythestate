package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.ArrayList;
import java.util.List;


public class ProfileJSON {
	private String name;
	private String description;
	private String tagname;
	List<TimeSpec> timeSpecs = new ArrayList<TimeSpec>();

	public ProfileJSON() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public List<TimeSpec> getTimeSpecs() {
		return timeSpecs;
	}

	public void setTimeSpecs(List<TimeSpec> timeSpecs) {
		this.timeSpecs = timeSpecs;
	}

	@Override
	public String toString() {
		return "ProfileJSON [description=" + description + ", name=" + name
				+ ", tagname=" + tagname + ", timeSpecs=" + timeSpecs + "]";
	}

}
