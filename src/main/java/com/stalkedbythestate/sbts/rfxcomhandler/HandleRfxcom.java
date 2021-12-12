package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleRfxcom implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HandleRfxcom.class);
	private Event event;
	private Event originalEvent;
	private Action action;
	private SbtsDeviceConfig sbtsConfig;
	private RfxcomController rfxcomController;
	private FreakApi freak;

	public HandleRfxcom(FreakApi freak, RfxcomController rfxcomController,
                        Event event, Event originalEvent, Action action) {
		this.freak = freak;
		this.event = event;
		this.originalEvent = originalEvent;
		this.action = action;
	}

	@Override
	public void run() {
		sbtsConfig = freak.getSbtsConfig();

		if (logger.isDebugEnabled())
			logger.debug("Have the sbts config");

	}

}
