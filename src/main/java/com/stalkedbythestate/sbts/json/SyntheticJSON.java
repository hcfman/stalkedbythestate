package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.Collection;
import java.util.HashSet;

public class SyntheticJSON {
	private Collection<String> triggerEventNames = new HashSet<String>();
	private String result;
	private int withinSeconds;

	public SyntheticJSON() {
	}

	public SyntheticJSON(String result, int withinSeconds) {
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

	public int getWithinSeconds() {
		return withinSeconds;
	}

	public void setWithinSeconds(int withinSeconds) {
		this.withinSeconds = withinSeconds;
	}

	@Override
	public String toString() {
		return "SyntheticJSON [result=" + result + ", triggerEventNames="
				+ triggerEventNames + ", withinSeconds=" + withinSeconds + "]";
	}

}
