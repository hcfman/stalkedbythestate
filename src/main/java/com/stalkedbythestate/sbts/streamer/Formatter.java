package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.diskwatchdog.DiskWatchdogHandler;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Progress;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class Formatter implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Formatter.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private LinkedBlockingQueue<Progress> progressQueue;
	private DiskWatchdogHandler diskWatchdogHandler = DiskWatchdogHandler
			.getHandler();
	private FreakApi freak;

	public Formatter(FreakApi freak) {
		this.freak = freak;
	}

	private boolean deletePartitions() {
		return true;
	}

	private boolean makeFilesystem() {
		return true;
	}

	@Override
	public void run() {
		/* Formatting could go in here */
	}

}
