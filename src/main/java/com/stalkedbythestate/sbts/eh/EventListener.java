package com.stalkedbythestate.sbts.eh;

import com.stalkedbythestate.sbts.eventlib.SyntheticTriggerEvent;

public interface EventListener {
	public void onEvent(SyntheticTriggerEvent event);
}
