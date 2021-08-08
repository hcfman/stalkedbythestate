package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.HasCameras;

import java.util.SortedSet;
import java.util.TreeSet;

public class VideoActionImpl extends AbstractAction implements HasCameras {
	private SortedSet<Integer> cameraSet = new TreeSet<Integer>();

	public VideoActionImpl(String name, String eventName, String description, SortedSet<Integer> cameraSet) {
		setName(name);
		setEventName(eventName);
		setDescription(description);
		setActionType(ActionType.ACTION_VIDEO);
		this.cameraSet = cameraSet;
	}

	public SortedSet<Integer> getCameraSet() {
		return cameraSet;
	}

}
