package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.SynthTrigger;

import java.util.Collection;
import java.util.HashSet;

public class SynthTriggerImpl implements SynthTrigger {
	Collection<String> triggerEventNames = new HashSet<String>();
	private String result;
	private int withinSeconds;
	
	public SynthTriggerImpl() {
	}

	public SynthTriggerImpl(String result, int withinSeconds, Collection<String> triggerEventNames) {
		this.result = result;
		this.withinSeconds = withinSeconds;
		this.triggerEventNames = triggerEventNames;
	}

	public Collection<String> getTriggerEventNames() {
		return triggerEventNames;
	}

	public void setTriggerEventNames(Collection<String> triggerEventNames) {
		this.triggerEventNames = triggerEventNames;
	}

	public int getWithinSeconds() {
		return withinSeconds;
	}

	public void setWithinSeconds(int withinSeconds) {
		this.withinSeconds = withinSeconds;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
