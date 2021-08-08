package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class TagButton {
	private String tagName;
	private String onEventName;
	private String offEventName;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getOnEventName() {
		return onEventName;
	}

	public void setOnEventName(String onEventName) {
		this.onEventName = onEventName;
	}

	public String getOffEventName() {
		return offEventName;
	}

	public void setOffEventName(String offEventName) {
		this.offEventName = offEventName;
	}

	@Override
	public String toString() {
		return "TagButton [offEventName=" + offEventName + ", onEventName="
				+ onEventName + ", tagName=" + tagName + "]";
	}
	
}
