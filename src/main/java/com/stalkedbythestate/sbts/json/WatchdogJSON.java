package com.stalkedbythestate.sbts.json;

import java.util.Collection;
import java.util.HashSet;

public class WatchdogJSON {
	private Collection<String> triggerEventNames = new HashSet<String>();
	private String result;
	private long withinSeconds;

	public WatchdogJSON() {
	}

	public WatchdogJSON(String result, int withinSeconds) {
		this.result = result;
		this.withinSeconds = withinSeconds;
	}

	public Collection<String> getTriggerEventNames() {
		return triggerEventNames;
	}

	public void setTriggerEventNames(Collection<String> triggerEventNames) {
		this.triggerEventNames = triggerEventNames;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getWithinSeconds() {
		return withinSeconds;
	}

	public void setWithinSeconds(long withinSeconds) {
		this.withinSeconds = withinSeconds;
	}

	@Override
	public String toString() {
		return "WatchdogJSON [result=" + result + ", triggerEventNames="
				+ triggerEventNames + ", withinSeconds=" + withinSeconds + "]";
	}

}
