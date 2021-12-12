package com.stalkedbythestate.sbts.dvrhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.dvr.VideoReader;
import com.stalkedbythestate.sbts.eventlib.*;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoActionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DvrHandler {
	private static final Logger logger = LoggerFactory.getLogger(DvrHandler.class);
	private final FreakApi freak;
	private final LinkedBlockingQueue<Event> eventQueue;
	private AtomicBoolean ready = new AtomicBoolean(false);
	private ConcurrentHashMap<Integer, LinkedBlockingQueue<Event>> cameraEventQueues;
	private SbtsDeviceConfig sbtsConfig;

	public DvrHandler(final FreakApi freak, final LinkedBlockingQueue<Event> eventQueue) {
		this.freak = freak;
		this.eventQueue = eventQueue;
	}

	public final void start() {
		handle();
	}

	// Actually spawn the readers, this can be called after a reconfigure event
	private synchronized void spawnReaders() {
		sbtsConfig = freak.getSbtsConfig();

		if (logger.isDebugEnabled())
			logger.debug("Spawning videoReaders");
		final Map<Integer, CameraDevice> cameraDevices = sbtsConfig.getCameraConfig()
				.getCameraDevices();
		final SortedSet<Integer> cameraOrder = sbtsConfig.getCameraConfig()
				.getCameraOrder();

		// A new set of queues
		cameraEventQueues = new ConcurrentHashMap<>();

		final Map<Integer, VideoReader> videoReaders = new HashMap<>();
		for (final int camera : cameraOrder) {
			if (logger.isDebugEnabled())
				logger.debug("Spawn videoReader: " + camera);
			cameraEventQueues.put(camera, new LinkedBlockingQueue<>());

			final VideoReader videoReader = new VideoReader(
					cameraDevices.get(camera), sbtsConfig.getSettingsConfig()
							.getConnectTimeout(), freak,
					cameraDevices.get(camera), cameraEventQueues.get(camera));
			videoReaders.put(camera, videoReader);
			videoReader.start();
		}
	}

	private void handle() {
		spawnReaders();

		final Thread thread = new Thread(() -> {
			ready.set(true);

			while (true) {
				Event event = null;
				try {
					event = eventQueue.take();
					if (logger.isDebugEnabled())
						logger.debug("dvr-handler event: " + event);
					if (event.getEventType() == EventType.EVENT_SHUTDOWN)
						return;

					switch (event.getEventType()) {
					case EVENT_CONFIGURE:
						if (logger.isDebugEnabled())
							logger.debug("Dvr CONFIGURE event");
						if (logger.isDebugEnabled())
							logger.debug("cameraEventQueues: "
									+ cameraEventQueues);
						for (final int cam : cameraEventQueues.keySet()) {
							if (logger.isDebugEnabled())
								logger.debug("Stop video for " + cam);
							final LinkedBlockingQueue<Event> queue = cameraEventQueues
									.get(cam);
							if (queue != null) {
								if (logger.isDebugEnabled())
									logger.debug("Stopping camera(" + cam
											+ ")");
								if (logger.isDebugEnabled())
									logger.debug("Sending down queue: "
											+ (Object) queue);
								queue.add(new ShutdownEvent());
								cameraEventQueues.remove(cam);
							} else {
								if (logger.isDebugEnabled())
									logger.debug("There was no queue for cam: "
											+ cam);
							}

						}

						// Wait till it settles
						sleepFiveSeconds();

						spawnReaders();
						break;
					case EVENT_SHUTDOWN:
						if (logger.isDebugEnabled())
							logger.debug("Shutting video down...");

						for (final int cam : cameraEventQueues.keySet()) {
							if (logger.isDebugEnabled())
								logger.debug("Switch video for " + cam);
							final LinkedBlockingQueue<Event> queue = cameraEventQueues
									.get(cam);
							if (queue != null) {
								if (logger.isDebugEnabled())
									logger.debug("Stopping camera(" + cam
											+ ")");
								queue.add(new ShutdownEvent());
								cameraEventQueues.remove(cam);
							}

						}

						// Wait till it settles
						sleepFiveSeconds();

						return;
					case EVENT_ACTION:
						if (logger.isDebugEnabled())
							logger.debug("Process EVENT_ACTION for Video Trigger");
						if (!(event instanceof SendActionEvent)) {
							if (logger.isDebugEnabled())
								logger.debug("Wrong type of event for Video Trigerring sending");
							break;
						}

						if (logger.isDebugEnabled())
							logger.debug("Looking good, cast action");
						final SendActionEvent sendActionEvent = (SendActionEvent) event;
						final Action action = sendActionEvent.getAction();

						if (!(action instanceof VideoActionImpl))
							break;

						// Don't write if the disk isn't setup
						if (sbtsConfig.getDiskConfig().getDiskState() != DiskState.ALL_GOOD)
							break;

						final VideoActionImpl videoActionImpl = (VideoActionImpl) action;
						for (final int cam : videoActionImpl.getCameraSet()) {
							if (logger.isDebugEnabled())
								logger.debug("Switch video for " + cam);
							final LinkedBlockingQueue<Event> queue = cameraEventQueues
									.get(cam);
							if (queue != null) {
								if (logger.isDebugEnabled())
									logger.debug("Camera is active, sending trigger: "
											+ videoActionImpl
													.getDescription());
								queue.add(new VideoTriggerEvent(cam,
										videoActionImpl.getDescription(),
										sendActionEvent.getEventTime()));
							}
						}

						break;

					}

				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				if (logger.isDebugEnabled())
					logger.debug("Got event: " + event);
			}

		});

		thread.setName("dvr-handler");
		thread.start();
	}

	private void sleepFiveSeconds() {
		try {
			Thread.sleep(5000);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isReady() {
		return ready.get();
	}

}
