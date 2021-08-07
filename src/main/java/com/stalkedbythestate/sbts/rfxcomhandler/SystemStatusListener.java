package com.stalkedbythestate.sbts.rfxcomhandler;

import com.stalkedbythestate.sbts.rfxcomlib.Packet;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import org.apache.log4j.Logger;

public class SystemStatusListener extends RfxComListenerImpl {
	private static final Logger rfxcomLogger = Logger.getLogger("rfxcom");
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
