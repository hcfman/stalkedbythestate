package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.config.Action;

import java.util.Comparator;

public class ActionsSortOrder implements Comparator<Action> {

	public int compare(Action o1, Action o2) {
		if (o1 == o2)
			return 0;

		if (!o1.getEventName().equals(o2.getEventName()))
			return o1.getEventName().compareTo(o2.getEventName());
		
		return o1.getName().compareTo(o2.getName());
	}

}
