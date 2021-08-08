package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;

import java.util.Collection;

public class WatchdogImpl implements Watchdog {
	private Collection<String> triggerEventNames;
	private String result;
	private long withinSeconds;

	public WatchdogImpl(final String result, final long withinSeconds, final Collection<String> triggerEventNames) {
		this.result = result;
		this.withinSeconds = withinSeconds;
		this.triggerEventNames = triggerEventNames;
	}

	public Collection<String> getTriggerEventNames() {
		return triggerEventNames;
	}

	public void setTriggerEventNames(final Collection<String> triggerEventNames) {
		this.triggerEventNames = triggerEventNames;
	}

	public long getWithinSeconds() {
		return withinSeconds;
	}

	public void setWithinSeconds(final long withinSeconds) {
		this.withinSeconds = withinSeconds;
	}

	public String getResult() {
		return result;
	}

	public void setResult(final String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "WatchdogImpl [triggerEventNames=" + triggerEventNames + ", result=" + result + ", withinSeconds="
				+ withinSeconds + "]";
	}
}
