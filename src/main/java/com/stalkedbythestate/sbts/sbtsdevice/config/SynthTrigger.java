package com.stalkedbythestate.sbts.sbtsdevice.config;

import java.util.Collection;

public interface SynthTrigger {
	public Collection<String> getTriggerEventNames();

	public void setTriggerEventNames(Collection<String> triggerEventNames);

	public int getWithinSeconds();

	public void setWithinSeconds(int withinSeconds);

	public String getResult();

	public void setResult(String result);
}
