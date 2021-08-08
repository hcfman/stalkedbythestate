package com.stalkedbythestate.sbts.phidgetqueuehandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.EventType;
import com.stalkedbythestate.sbts.eventlib.SendActionEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetActionImpl;
import com.phidget22.DigitalOutput;
import com.phidget22.PhidgetException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class PhidgetPortController implements Runnable {
	private static final Logger logger = Logger
			.getLogger(PhidgetPortController.class);
	private static final Logger phidgetLogger = Logger.getLogger("phidget");
	private FreakApi freak;
	private LinkedBlockingQueue<Event> eventQueue;
	private PhidgetDevice phidgetDevice;
	private List<DigitalOutput> digitalOutputList;
	private int port = 0;

	public PhidgetPortController(List<DigitalOutput> digitalOutputList,
			PhidgetDevice phidgetDevice, int port, FreakApi freak,
			LinkedBlockingQueue<Event> eventQueue) {
		this.digitalOutputList = digitalOutputList;
		this.phidgetDevice = phidgetDevice;
		this.port = port;
		this.freak = freak;
		this.eventQueue = eventQueue;
	}

	public PhidgetDevice getPhidget() {
		return phidgetDevice;
	}

	public int getPort() {
		return port;
	}

	private void executeAction(Action action) {
		PhidgetActionImpl phidgetActionImpl = (PhidgetActionImpl) action;

		switch (phidgetActionImpl.getPhidgetActionType()) {
			case On:
				if (logger.isDebugEnabled())
					logger.debug("Set phidget ("
							+ phidgetActionImpl.getPhidgetName() + ") port ("
							+ phidgetActionImpl.getPort() + "): on");
				try {
					digitalOutputList.get(port).setState(!phidgetDevice.getInitialOutputState()[port]);
				} catch (PhidgetException e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception setting port state true");
				}
				phidgetLogger.info("Phidget ["
						+ phidgetActionImpl.getPhidgetName() + "] Set port("
						+ port + ") -> On");
				break;
		case Off:
			try {
				if (logger.isDebugEnabled())
					logger.debug("Set phidget ("
							+ phidgetActionImpl.getPhidgetName() + ") port ("
							+ phidgetActionImpl.getPort() + "): off");
				digitalOutputList.get(port).setState(phidgetDevice.getInitialOutputState()[port]);
				phidgetLogger.info("Phidget ["
						+ phidgetActionImpl.getPhidgetName() + "] Set port("
						+ port + ") -> Off");
			} catch (PhidgetException e) {
				if (logger.isDebugEnabled())
					logger.debug("Exception setting port state false");
				e.printStackTrace();
			}
			break;
		case Pulse:
			if (logger.isDebugEnabled())
				logger.debug("Set phidget ("
						+ phidgetActionImpl.getPhidgetName() + ") port ("
						+ phidgetActionImpl.getPort() + "): pulse ("
						+ phidgetActionImpl.getPulseTrain() + ")");
			String trainString = phidgetActionImpl.getPulseTrain();
			String[] pulseTimeStrings = trainString.split(",");
			int[] pulseTimes = new int[pulseTimeStrings.length];
			for (int i = 0; i < pulseTimes.length; i++) {
				try {
					pulseTimes[i] = Integer.parseInt(pulseTimeStrings[i]);
				} catch (NumberFormatException e) {
					if (logger.isDebugEnabled())
						logger.debug("Invalid pulse time ("
								+ pulseTimeStrings[i] + ")");
					e.printStackTrace();
					return;
				}
			}

			boolean state = !phidgetDevice.getInitialOutputState()[port];
			for (int pulseTime : pulseTimes) {
				try {
					digitalOutputList.get(port).setState(state);
					if (logger.isDebugEnabled())
						logger.debug("Pulse port (" + port + ") state: "
								+ state);
				} catch (PhidgetException e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception setting phidget ("
								+ phidgetDevice.getName() + ") port (" + port
								+ ") to state (" + state + ")");
					e.printStackTrace();
				}

				try {
					Thread.sleep(pulseTime);
				} catch (InterruptedException e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception sleeping for time ("
								+ pulseTime + ")");
					e.printStackTrace();
				}
				state = !state;
			}

			// Set back to initial output state
			try {
				digitalOutputList.get(port).setState(phidgetDevice.getInitialOutputState()[port]);
			} catch (PhidgetException e) {
				if (logger.isDebugEnabled())
					logger.debug("Exception setting phidget ("
							+ phidgetDevice.getName() + ") port (" + port
							+ ") to state (" + state + ")");
				e.printStackTrace();
			}

			phidgetLogger.info("Phidget [" + phidgetActionImpl.getPhidgetName()
					+ "] Set port(" + port + ") -> Pulse ("
					+ phidgetActionImpl.getPulseTrain() + ")");
			break;
		}

	}

	@Override
	public void run() {
		if (logger.isDebugEnabled())
			logger.debug("Starting phidget port handler run()");

		while (true) {
			Event event = null;
			try {
				event = eventQueue.take();
				if (event.getEventType() == EventType.EVENT_SHUTDOWN)
					return;

				switch (event.getEventType()) {
				case EVENT_CONFIGURE:
					break;
				case EVENT_SHUTDOWN:
					return;
				case EVENT_ACTION:
					if (logger.isDebugEnabled())
						logger.debug("Process EVENT_ACTION for PhidgetPortController");
					if (!(event instanceof SendActionEvent)) {
						if (logger.isDebugEnabled())
							logger.debug("Wrong type of event for PhidgetPortController sending");
						break;
					}

					if (logger.isDebugEnabled())
						logger.debug("Looking good, cast action");
					SendActionEvent sendActionEvent = (SendActionEvent) event;
					Action action = sendActionEvent.getAction();

					if (!(action instanceof PhidgetActionImpl))
						break;

					if (logger.isDebugEnabled())
						logger.debug("All green for HTTP action");
					executeAction(action);
					break;
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
