package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.rfxcomlib.Packet;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemStatusListener extends RfxComListenerImpl {
	private static final Logger rfxcomLogger = LoggerFactory.getLogger("rfxcom");
	private RfxcomController rfxcomController;

	public SystemStatusListener(RfxcomController rfxcomController, RfxcomCommand command) {
		super(command);
		this.rfxcomController = rfxcomController;
	}
	
	@Override
	public void fire(Packet packet) {
		rfxcomController.setConnected(true);
		rfxcomLogger.info("Received initial status response, connected!");
//		System.out.println("Received initial status response, connected!");
	}

	@Override
	public String toString() {
		return "SystemStatusListener [rfxcomController=" + rfxcomController
				+ "]";
	}

}
