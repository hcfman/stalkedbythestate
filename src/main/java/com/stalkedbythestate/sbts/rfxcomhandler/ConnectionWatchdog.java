package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionWatchdog implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectionWatchdog.class);
	private static final Logger rfxcomLogger = LoggerFactory.getLogger("rfxcom");
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private final int RETRY_TIME = 2000;
	private final LinkedBlockingQueue<Event> watchdogQueue;
	private FileInputStream fis = null;
	private FileOutputStream fos = null;
	private final Executor packetReaderExecutor = Executors.newSingleThreadExecutor();
	private final FreakApi freak;
	private final SbtsDeviceConfig sbtsConfig;
	private final RfxcomController rfxcomController;

	public ConnectionWatchdog(RfxcomController rfxcomController,
							  SbtsDeviceConfig sbtsConfig, FreakApi freak,
							  LinkedBlockingQueue<Event> watchdogQueue) {
		logger.debug("Creating ConnectionWatchdog");
		this.rfxcomController = rfxcomController;
		this.sbtsConfig = sbtsConfig;
		this.freak = freak;
		this.watchdogQueue = watchdogQueue;
	}

	private void setTTYspeed() {
		logger.debug("Setting tty speed");
		ScriptRunner scriptRunner = new ScriptRunner();

		scriptRunner.spawn(freak.getSbtsBase() + "/bin/setspeed.sh");
	}

	@Override
	public void run() {
		logger.debug("Starting connection watchdog");
		boolean connected = false;
		File ttyFile = new File("/dev/ttyUSB0");

		while (true) {
			boolean changed = (connected && (!ttyFile.exists()
					|| !ttyFile.canRead() || !ttyFile.canWrite()))
					|| (!connected && (ttyFile.exists() && ttyFile.canRead() && ttyFile
							.canWrite()));

			/*
			 * Connect or disconnect if connection status changed
			 */
			if (changed) {
				logger.debug("Connected status changed");
				if (!connected) {
					// Should be done with udev
					setTTYspeed();
					try {
						fis = new FileInputStream(ttyFile);
						fos = new FileOutputStream(ttyFile);
					} catch (FileNotFoundException e) {
						logger.error("Can't open input streams", e);
						opLogger.error("New USB device added, but I'm having trouble reading from it to establish it"
								+ " as a RFXCOM device");
					}

					if (fis != null && fos != null) {
						packetReaderExecutor.execute(new HandlePackets(
								rfxcomController, fis, fos));
					} else {
						try {
							if (fis != null)
								fis.close();
							if (fos != null)
								fos.close();

							fis = null;
							fos = null;
						} catch (IOException e) {
							logger.error("Exception closing USB streams", e);
						}
					}
					connected = true;
				} else {
					rfxcomController.setConnected(false);
					rfxcomLogger.info("RFXCOM stopped, device probably removed");
					
					try {
						/*
						 * Thread should get an exception and die after this.
						 */
						if (fis != null)
							fis.close();
						if (fos != null)
							fos.close();

						fis = null;
						fos = null;

					} catch (IOException e) {
						logger.error("Exception closing USB streams", e);
					}

					connected = false;
				}
			}

			/*
			 * Sleep then try again
			 */
			try {
				Thread.sleep(RETRY_TIME);
			} catch (InterruptedException e) {
				logger.error("Exception whilst sleeping", e);
			}
		}

	}

}
