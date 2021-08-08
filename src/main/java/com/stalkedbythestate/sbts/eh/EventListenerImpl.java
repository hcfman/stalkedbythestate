package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.SyntheticTriggerEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.log4j.Logger;

public class EventListenerImpl implements EventListener {
	private static final Logger logger = Logger.getLogger(EventListenerImpl.class);
	private FreakApi freak;

	public EventListenerImpl(FreakApi freak) {
		this.freak = freak;
	}

	public void onEvent(SyntheticTriggerEvent event) {
		if (logger.isDebugEnabled())
			logger.debug("Got event: " + event);

		freak.sendEvent(event);	}
}
