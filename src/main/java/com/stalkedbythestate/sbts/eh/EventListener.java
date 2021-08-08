package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.SyntheticTriggerEvent;

public interface EventListener {
	public void onEvent(SyntheticTriggerEvent event);
}
