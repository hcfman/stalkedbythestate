package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.SyntheticTriggerEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListenerImpl implements EventListener {
	private static final Logger logger = LoggerFactory.getLogger(EventListenerImpl.class);
	private FreakApi freak;

	public EventListenerImpl(FreakApi freak) {
		this.freak = freak;
	}

	public void onEvent(SyntheticTriggerEvent event) {
		if (logger.isDebugEnabled())
			logger.debug("Got event: " + event);

		freak.sendEvent(event);	}
}
