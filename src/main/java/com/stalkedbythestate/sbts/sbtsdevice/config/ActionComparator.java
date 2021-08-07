package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.util.Comparator;

public class ActionComparator implements Comparator<Action> {

	@Override
	public int compare(Action o1, Action o2) {
		return o1.getActionType().compareTo(o2.getActionType());
	}

}
