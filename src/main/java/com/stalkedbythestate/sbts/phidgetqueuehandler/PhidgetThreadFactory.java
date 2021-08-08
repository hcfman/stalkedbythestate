package com.stalkedbythestate.sbts.phidgetqueuehandler;

// Copyright (c) 2021 Kim Hendrikse

import java.util.concurrent.ThreadFactory;

public class PhidgetThreadFactory implements ThreadFactory {
	
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		PhidgetController phidgetHandler = (PhidgetController) runnable;
		thread.setName("phidget-" + phidgetHandler.getPhidgetDevice().getSerialNumber());
		return thread;
	}

}
