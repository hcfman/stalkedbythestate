package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class ActionConfig {
	private volatile List<Action> actionList = new ArrayList<Action>();

	public void add(Action action) {
		actionList.add(action);
	}
	
	public void add(int index, Action action) {
		actionList.add(index, action);
	}
	
	public Action get(int index) {
		return actionList.get(index);
	}
	
	public void remove(Action action) {
		actionList.remove(action);
	}
	
	public void remove(int index) {
		actionList.remove(index);
	}
	
	public List<Action> getActionList() {
		return actionList;
	}

	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}
	
}
