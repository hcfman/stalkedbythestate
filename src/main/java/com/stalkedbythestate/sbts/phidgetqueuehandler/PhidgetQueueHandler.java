package com.stalkedbythestate.sbts.phidgetqueuehandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.EventType;
import com.stalkedbythestate.sbts.eventlib.SendActionEvent;
import com.stalkedbythestate.sbts.eventlib.ShutdownEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetActionImpl;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class PhidgetQueueHandler {
	private static final Logger logger = Logger
			.getLogger(PhidgetQueueHandler.class);
	private FreakApi freak;
	private LinkedBlockingQueue<Event> eventQueue;
	private SbtsDeviceConfig sbtsConfig;
	private Map<Integer, LinkedBlockingQueue<Event>> phidgetQueue = new HashMap<Integer, LinkedBlockingQueue<Event>>();
	private List<Integer> phidgetList = new ArrayList<Integer>();

	public PhidgetQueueHandler(FreakApi freak,
			LinkedBlockingQueue<Event> eventQueue) {
		this.freak = freak;
		this.eventQueue = eventQueue;

		sbtsConfig = freak.getSbtsConfig();
	}

	private void startControllers() {
		if (logger.isDebugEnabled())
			logger.debug("Starting PhidgetQueueHandler handler");
		for (PhidgetDevice phidgetDevice : sbtsConfig.getPhidgetConfig()
				.getPhidgetMap().values()) {
			int serialNum = phidgetDevice.getSerialNumber();
			phidgetQueue.put(serialNum, new LinkedBlockingQueue<Event>());

			// Save serial number so can shutdown before restarting
			phidgetList.add(serialNum);

			// Spawn the phidget controller
			PhidgetThreadFactory phidgetThreadFactory = new PhidgetThreadFactory();
			if (logger.isDebugEnabled())
				logger.debug("Start new thread");
			phidgetThreadFactory.newThread(
					new PhidgetController(phidgetDevice, freak, phidgetQueue
							.get(phidgetDevice.getSerialNumber()))).start();
		}
	}

	private void stopControllers() {
		if (logger.isDebugEnabled())
			logger.debug("Stopping all of the phidget controllers");
		for (int serialNum : phidgetList) {
			LinkedBlockingQueue<Event> queue = phidgetQueue.get(serialNum);
			if (queue == null) {
				if (logger.isDebugEnabled())
					logger.debug("Phidget (" + phidgetQueue
							+ ") has not been setup");
				break;
			}

			try {
				queue.put(new ShutdownEvent());
			} catch (InterruptedException e) {
				logger.error("Interrupted exception whilst putting ConfigureEvent on the queue: "
						+ e.getMessage());
			}
		}
	}

	public void start() {

		Thread thread = new Thread() {

			@Override
			public void run() {
				startControllers();

				while (true) {
					Event event = null;
					try {
						event = eventQueue.take();
						if (event.getEventType() == EventType.EVENT_SHUTDOWN)
							return;

						switch (event.getEventType()) {
						case EVENT_CONFIGURE:
							if (logger.isDebugEnabled())
								logger.debug("Got a configuration event");
							stopControllers();
							delayOneSecond();
							startControllers();
							if (logger.isDebugEnabled())
								logger.debug("Restarted controllers");
							break;
						case EVENT_SHUTDOWN:
							return;
						case EVENT_ACTION:
							if (logger.isDebugEnabled())
								logger.debug("Process EVENT_ACTION for PhidgetActionImpl");
							if (!(event instanceof SendActionEvent)) {
								if (logger.isDebugEnabled())
									logger.debug("Wrong type of event for PhidgetActionImpl sending");
								break;
							}

							if (logger.isDebugEnabled())
								logger.debug("Looking good, cast action");
							SendActionEvent sendActionEvent = (SendActionEvent) event;
							Action action = sendActionEvent.getAction();

							if (!(action instanceof PhidgetActionImpl))
								break;

							PhidgetActionImpl phidgetActionImpl = (PhidgetActionImpl) action;
							PhidgetDevice selectedPhidget = sbtsConfig
									.getPhidgetConfig().getPhidgetMap()
									.get(phidgetActionImpl.getPhidgetName());

							if (selectedPhidget == null) {
								if (logger.isDebugEnabled())
									logger.debug("Invalid phidget name");
								break;
							}

							LinkedBlockingQueue<Event> queue = phidgetQueue
									.get(selectedPhidget.getSerialNumber());
							if (queue == null) {
								if (logger.isDebugEnabled())
									logger.debug("Phidget (" + phidgetQueue
											+ ") has not been setup");
								break;
							}

							queue.put(event);

							if (logger.isDebugEnabled())
								logger.debug("All green for HTTP action");
							// executor.execute(new HandleHttp(event,
							// sendActionEvent.getOriginalEvent(), action));
							break;
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			private void delayOneSecond() {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.error("Caught exception whilst sleeping");
				}
			}
		};

		thread.setName("phidget-queue-handler");
		thread.start();

	}

}
