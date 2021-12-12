package com.stalkedbythestate.sbts.diskwatchdog;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DiskWatchdogHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(DiskWatchdogHandler.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private static final long MOUNT_RETRY_TIME = 10000;
	private volatile boolean formatting = false;
	private SbtsDeviceConfig sbtsConfig;
	private static DiskWatchdogHandler handler;
	private volatile boolean mounted = false;
	private volatile boolean dontMount = false;
	private volatile boolean diskAvailable = false;
	private DiskState oldState;
	private DiskState newState;
	private FreakApi freak;

	public DiskWatchdogHandler() {
		handler = this;
	}

	public boolean isMounted() {
		return mounted;
	}

	public void setFormatting(boolean formatting) {
		this.formatting = formatting;
		if (formatting) {
			sbtsConfig.getDiskConfig().setDiskState(DiskState.FORMATTING);
			sbtsConfig.getDiskConfig().setLastMessage("Formatting");
			newState = DiskState.FORMATTING;
		} else {
			sbtsConfig.getDiskConfig().setDiskState(DiskState.UN_INITIALISED);
			sbtsConfig.getDiskConfig().setLastMessage("Finished formatting");
			newState = oldState = DiskState.UN_INITIALISED;
		}
	}

	public static DiskWatchdogHandler getHandler() {
		return handler;
	}

	public synchronized boolean unmount() {
		return true;
	}

	public final void start(FreakApi freak) {
		this.freak = freak;

		handle();
	}

	private void setMounted(boolean mounted) {
		this.mounted = mounted;
	}

	private boolean isDontMount() {
		return dontMount;
	}

	private void setDontMount(boolean dontMount) {
		this.dontMount = dontMount;
	}

	private void setDiskAvailable(boolean diskAvailable) {
		this.diskAvailable = diskAvailable;
	}

	private boolean isFormatting() {
		return formatting;
	}

	private boolean couldMount() {
		// Don't mount when formatting
		if (isFormatting())
			return false;

		if (freak.mountReadonly()) {
			opLogger.info("Disk mounted");
			setMounted(true);
			return true;
		} else {
			setMounted(false);
			return false;
		}
	}

	private void handle() {
		sbtsConfig = freak.getSbtsConfig();

		Thread thread = new Thread() {

			@Override
			public void run() {
				long lastMountAttempt = 0;
				File dvrImagesFile = new File(freak.getSbtsBase()
						+ "/disk/sbts/images");
				File eventsFile = new File(freak.getSbtsBase()
						+ "/disk/sbts/events");
				File tmpImagesFile = new File(freak.getSbtsBase()
						+ "/disk/sbts/tmp_images");

				oldState = DiskState.UN_INITIALISED;
				while (true) {
					newState = oldState;

					setDiskAvailable(true);

					if (!isMounted() && !isDontMount()) {
						if (System.currentTimeMillis()
								- lastMountAttempt >= MOUNT_RETRY_TIME) {
							lastMountAttempt = System
									.currentTimeMillis();
							if (!couldMount()) {
								if (!isFormatting()) {
									opLogger.info("Failed to mount drive");
									sbtsConfig
											.getDiskConfig()
											.setLastMessage(
													"Failed to mount disk, try formatting");
								}
							} else {
								if (!dvrImagesFile.isDirectory()
										|| !dvrImagesFile.canWrite()
										|| !dvrImagesFile.canRead()
										|| !eventsFile.isDirectory()
										|| !eventsFile.canRead()
										|| !eventsFile.canWrite()
										|| !tmpImagesFile.isDirectory()
										|| !tmpImagesFile.canRead()
										|| !tmpImagesFile.canWrite()) {
									newState = DiskState.PARTITION_MOUNTED_NO_STRUCTURE;
									opLogger.info("Disk was mounted but it is not correctly formatted. Please format the disk.");
									setDontMount(true);
									unmount();
									sbtsConfig
											.getDiskConfig()
											.setLastMessage(
													"Drive was mountable, but did not contain a valid structure, try formatting");
								} else {
									newState = DiskState.ALL_GOOD;
									opLogger.info("Disk was mounted and is correctly formatted");
									sbtsConfig
											.getDiskConfig()
											.setLastMessage(
													"Drive mounted successfully");
								}
							}
						}
					}

					sbtsConfig.getDiskConfig().setDiskState(newState);

					oldState = newState;

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						if (logger.isDebugEnabled())
							if (logger.isDebugEnabled())
								logger.debug("Interrupted sleep in disk watchdog");
					}
				}
			}

		};

		thread.setName("disk-watchdog");
		thread.start();
	}

}
