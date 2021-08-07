package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.util.HashMap;
import java.util.Map;

public class SynthTriggerConfig {
	private Map<String, SynthTrigger> triggerMap = new HashMap<String, SynthTrigger>();

	public void put(String eventName, SynthTrigger trigger) {
		triggerMap.put(eventName, trigger);
	}
	
	public SynthTrigger get(String eventName) {
		return triggerMap.get(eventName);
	}
	
	public void remove(String eventName) {
		triggerMap.remove(eventName);
	}
	
	public Map<String, SynthTrigger> getTriggerMap() {
		return triggerMap;
	}

	public void setTriggerMap(Map<String, SynthTrigger> triggerMap) {
		this.triggerMap = triggerMap;
	}

	@Override
	public String toString() {
		return "SynthTriggerConfig [triggerMap=" + triggerMap + "]";
	}
}
