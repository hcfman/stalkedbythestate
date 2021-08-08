package com.stalkedbythestate.sbts.phidgetqueuehandler;

// Copyright (c) 2021 Kim Hendrikse

import java.util.concurrent.ThreadFactory;

public class PhidgetPortThreadFactory implements ThreadFactory {
	
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		PhidgetPortController phidgetPortHandler = (PhidgetPortController) runnable;
		thread.setName("phidget-" + phidgetPortHandler.getPhidget().getSerialNumber()
				+ "-" + phidgetPortHandler.getPort());
		return thread;
	}

}
