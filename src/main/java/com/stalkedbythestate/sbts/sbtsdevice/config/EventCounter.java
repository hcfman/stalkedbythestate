package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EventCounter {
	private Set<Long> eventSet = new HashSet<Long>();
	private int withinSeconds;
	private int count;
	
	public EventCounter() {
	}

	public EventCounter(int count, int withinSeconds) {
		this.withinSeconds = withinSeconds;
		this.count = count;
	}
	
	public int getWithinSeconds() {
		return withinSeconds;
	}

	public void setWithinSeconds(int withinSeconds) {
		this.withinSeconds = withinSeconds;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean shouldFire() {
		long now = System.currentTimeMillis();
		
		// Expire times
		Iterator<Long> iterator = eventSet.iterator();
		while (iterator.hasNext()) {
			long next = iterator.next();
			if ((now - next) / 1000 > withinSeconds)
				iterator.remove();
		}
		
		eventSet.add(now);
		
		if (eventSet.size() >= count) {
			eventSet.clear();
			return true;
		}
		
		return false;
		
	}

}
