package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;

import java.util.SortedSet;
import java.util.TreeSet;

public class EmailActionImpl extends AbstractAction {
	private String to;
	private VideoType videoType;
	private SortedSet<Integer> cameraSet = new TreeSet<Integer>();
	private String responseGroup;

	public EmailActionImpl() {
	}

	public EmailActionImpl(String name, String eventName, String description, String to) {
		setName(name);
		setEventName(eventName);
		setDescription(description);
		setActionType(ActionType.ACTION_EMAIL);
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public VideoType getVideoType() {
		return videoType;
	}

	public void setVideoType(VideoType videoType) {
		this.videoType = videoType;
	}
	
	public void add(int cameraIndex) {
		cameraSet.add(cameraIndex);
	}
	
	public SortedSet<Integer> getCameraSet() {
		return cameraSet;
	}

	public void setCameraSet(SortedSet<Integer> cameraSet) {
		this.cameraSet = cameraSet;
	}

	public String getResponseGroup() {
		return responseGroup;
	}

	public void setResponseGroup(String responseGroup) {
		this.responseGroup = responseGroup;
	}


}
