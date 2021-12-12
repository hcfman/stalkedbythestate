package com.stalkedbythestate.sbts.phidgetqueuehandler;

// Copyright (c) 2021 Kim Hendrikse

import com.phidget22.*;
import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.PhidgetTriggerEvent;
import com.stalkedbythestate.sbts.eventlib.SendActionEvent;
import com.stalkedbythestate.sbts.eventlib.ShutdownEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetActionImpl;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class PhidgetController implements Runnable {
    private static final Logger logger = LoggerFactory
            .getLogger(PhidgetController.class);
    private static final Logger opLogger = LoggerFactory.getLogger("operations");
    private static final Logger phidgetLogger = LoggerFactory.getLogger("phidget");
    private final FreakApi freak;
    private final LinkedBlockingQueue<Event> eventQueue;
    private final Map<Integer, LinkedBlockingQueue<Event>> phidgetPortQueue = new HashMap<>();
    private volatile PhidgetDevice phidgetDevice;
    private final Map<Integer, PhidgetPortController> portControllerMap = new ConcurrentHashMap<>();
    private final int[] eventCount = new int[PhidgetConstants.PHIDGET_PORT_SIZE];

    private final List<DigitalInput> digitalInputs;
    private final List<DigitalOutput> digitalOutputs;

    private final List<AttachListener> inputAttachListenerList;
    private final List<AttachListener> outputAttachListenerList;
    private final List<DetachListener> inputDetachListenerList;
    private final List<DetachListener> outputDetachListenerList;
    private final List<ErrorListener> errorListenerList;
    private final List<DigitalInputStateChangeListener> inputChangeListenerList;

    public PhidgetController(final PhidgetDevice phidget, final FreakApi freak,
                             final LinkedBlockingQueue<Event> eventQueue) {
        this.phidgetDevice = phidget;
        this.freak = freak;
        this.eventQueue = eventQueue;

        digitalInputs = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        digitalOutputs = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        inputAttachListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        outputAttachListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        inputDetachListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        outputDetachListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        errorListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));
        inputChangeListenerList = Collections.synchronizedList(new ArrayList<>(phidget.getPortSize()));

        try {
            for (int i = 0; i < phidgetDevice.getPortSize(); i++) {
                final DigitalInput digitalInput = new DigitalInput();
                logger.debug("new DigitalInput() for port " + i);
                digitalInput.setDeviceSerialNumber(phidget.getSerialNumber());
                logger.debug("digitalInput.setDeviceSerialNumber(" + phidget.getSerialNumber() + ")");
                digitalInput.setChannel(i);
                logger.debug("digitalInput.setChannel(" + i + ")");
                digitalInputs.add(i, digitalInput);

                final DigitalOutput digitalOutput = new DigitalOutput();
                logger.debug("new DigitalOutput() for port " + i);
                digitalOutput.setDeviceSerialNumber(phidget.getSerialNumber());
                logger.debug("digitalOutput.setDeviceSerialNumber(" + phidget.getSerialNumber() + ")");
                digitalOutput.setChannel(i);
                logger.debug("digitalOutput.setChannel(" + i + ")");
                digitalOutputs.add(i, digitalOutput);
            }
        } catch (final PhidgetException e) {
            e.printStackTrace();
        }

        if (logger.isDebugEnabled())
            logger.debug("Have setup the PhidgetController handler");
    }

    public PhidgetDevice getPhidgetDevice() {
        return phidgetDevice;
    }

    private void initialise(final int port, final boolean isInput) throws PhidgetException {
        if (logger.isDebugEnabled())
            logger.debug("phidget: " + phidgetDevice);
        if (logger.isDebugEnabled())
            logger.debug("i: " + port);

        try {
            if (isInput) {
                getPhidgetDevice().setConnectedInput(port, true);
                logger.debug("getPhidgetDevice().setConnectedInput(" + port + ", true)");
            } else {
                getPhidgetDevice().setConnectedOutput(port, true);
                logger.debug("getPhidgetDevice().setConnectedOutput(" + port + ", true)");
                digitalOutputs.get(port).setState(phidgetDevice.getInitialOutputState()[port]);
                phidgetLogger.info("Phidget ["
                        + phidgetDevice.getSerialNumber()
                        + "] Set port ("
                        + port
                        + ") -> "
                        + (phidgetDevice.getInitialOutputState()[port] ? "On"
                        : "Off"));
            }
        } catch (PhidgetException e) {
            e.printStackTrace();
        }
    }

    private void startPhidgetListeners() {
        logger.debug("startPhidgetListeners()");
        for (int i = 0; i < phidgetDevice.getPortSize(); i++) {
            inputAttachListenerList.add(i, getAttachListener(i, true));
            digitalInputs.get(i).addAttachListener(inputAttachListenerList.get(i));
            logger.debug("Add input attach listener for i " + i);

            outputAttachListenerList.add(i, getAttachListener(i, false));
            digitalOutputs.get(i).addAttachListener(outputAttachListenerList.get(i));
            logger.debug("Add output attach listener for i " + i);

            inputDetachListenerList.add(i, getDetachListener(i, true));
            digitalInputs.get(i).addDetachListener(inputDetachListenerList.get(i));

            outputDetachListenerList.add(i, getDetachListener(i, false));
            digitalOutputs.get(i).addDetachListener(outputDetachListenerList.get(i));

            errorListenerList.add(i, getErrorListener());

            digitalInputs.get(i).addErrorListener(errorListenerList.get(i));

            inputChangeListenerList.add(i, getDigitalInputStateChangeListener());
            digitalInputs.get(i).addStateChangeListener(inputChangeListenerList.get(i));

            if (logger.isDebugEnabled()) {
                logger.debug("Want to open phidget: "
                        + phidgetDevice.getSerialNumber());
            }

        } // For loop

    }

    private void startPhidgetPortHandlers() {
        logger.debug("startPhidgetPortHandlers()");
        for (int i = 0; i < PhidgetConstants.PHIDGET_PORT_SIZE; i++) {
            if (logger.isDebugEnabled())
                logger.debug("Setting phidgetPortQueue for phidget with serial number " + getPhidgetDevice().getSerialNumber() + "("
                        + i + ")");
            phidgetPortQueue.put(i, new LinkedBlockingQueue<>());

            final PhidgetPortThreadFactory phidgetPortThreadFactory = new PhidgetPortThreadFactory();
            if (logger.isDebugEnabled())
                logger.debug("Start new thread");
            final PhidgetPortController portController = new PhidgetPortController(
                    digitalOutputs, phidgetDevice, i, freak,
                    phidgetPortQueue.get(i));
            portControllerMap.put(i, portController);
            phidgetPortThreadFactory.newThread(portController).start();
        }

    }

    private void openPhidgetPorts() {
        logger.debug("openPhidgetPorts()");
        for (int i = 0; i < getPhidgetDevice().getPortSize(); i++ ) {
            try {
                digitalInputs.get(i).open(0);
                digitalOutputs.get(i).open(0);
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        }
    }

    private ErrorListener getErrorListener() {
        return ee -> {
            if (logger.isDebugEnabled())
                logger.debug("error event for " + ee);
        };
    }

    private DigitalInputStateChangeListener getDigitalInputStateChangeListener() {
        return ae -> {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Input changed for: "
                            + ae.getSource().getChannel() + " to "
                            + ae.getState());
                }
                eventCount[ae.getSource().getChannel()]++;
                // The first one is initialization
                if (eventCount[ae.getSource().getChannel()] == 1)
                    return;

                final String eventName;
                if (logger.isDebugEnabled())
                    logger.debug("phidgetDevice: "
                            + phidgetDevice.getName());
                if (phidgetDevice.getInitialInputState()[ae.
                        getSource().getChannel()] ^ ae.getState()) {
                    if (logger.isDebugEnabled())
                        logger.debug("State: true");
                    eventName = phidgetDevice.getOnTriggerEventNames()[ae.getSource().getChannel()];
                } else {
                    if (logger.isDebugEnabled())
                        logger.debug("State: false");
                    eventName = phidgetDevice
                            .getOffTriggerEventNames()[ae
                            .getSource().getChannel()];
                }

                if (eventName != null) {
                    if (logger.isDebugEnabled())
                        logger.debug("Callback on phidget state change "
                                + phidgetDevice.getName()
                                + ": "
                                + ae.getSource().getChannel()
                                + " with eventName ("
                                + eventName
                                + ") => ");
                    freak.sendEvent(new PhidgetTriggerEvent(
                            eventName, System.currentTimeMillis()));
                } else {
                    if (logger.isDebugEnabled())
                        logger.debug("eventName is null, index: "
                                + ae.getSource().getChannel());
                }

                phidgetLogger.info("Phidget["
                        + ae.getSource().getDeviceSerialNumber()
                        + "] Port ("
                        + ae.getSource().getChannel()
                        + ") <- "
                        + (phidgetDevice.getInitialInputState()[ae
                        .getSource().getChannel()] ^ ae.getState() ? "On"
                        : "Off"));
            } catch (final PhidgetException e) {
                e.printStackTrace();
            }

        };
    }

    private DetachListener getDetachListener(final int port, final boolean isInput) {
        return ae -> {
            try {
                logger.debug("detachment of " + ae.getSource().getDeviceSerialNumber());
                opLogger.info("Phidget ["
                        + ae.getSource().getDeviceSerialNumber()
                        + "], " + (isInput ? "input" : "output") + " port(" + port + " ) disconnected");
                phidgetLogger.info("Phidget ["
                        + ae.getSource().getDeviceSerialNumber()
                        + "], " + (isInput ? "input" : "output") + " port(" + port + ") disconnected");
            } catch (final PhidgetException e) {
                phidgetLogger
                        .error("Error detaching from Phidget: "
                                + e.getMessage());
                e.printStackTrace();
            } finally {
                if (isInput) {
                    getPhidgetDevice().setConnectedInput(port, false);
                    logger.debug("getPhidgetDevice().setConnectedInput(" + port + ", true)");
                } else {
                    getPhidgetDevice().setConnectedOutput(port, false);
                    logger.debug("getPhidgetDevice().setConnectedOutput(" + port + ", true)");
                }
            }
        };
    }

    private AttachListener getAttachListener(final int port, final boolean isInput) {
        return ae -> {
            try {
                logger.debug("attachment of "
                        + ae.getSource().getDeviceSerialNumber());
                if (isInput) {
                    getPhidgetDevice().setConnectedInput(port, true);
                } else {
                    getPhidgetDevice().setConnectedOutput(port, true);
                }
                initialise(port, isInput);
                if (getPhidgetDevice().isConnected()) {
                    opLogger.info("Phidget ["
                            + ae.getSource().getDeviceSerialNumber()
                            + "], connected");
                }
                phidgetLogger.info("Phidget ["
                        + ae.getSource().getDeviceSerialNumber()
                        + "], " + (isInput ? "input" : "output") + " port (" + port + ") connected");
            } catch (final PhidgetException e) {
                phidgetLogger
                        .error("Error attaching to Phidget: "
                                + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private void start() {
        if (logger.isDebugEnabled())
            logger.debug("Try starting phidget start(): "
                    + phidgetDevice.getSerialNumber());

        // Start the listeners. These handle input events from the phidget
        startPhidgetListeners();
        startPhidgetPortHandlers();
        openPhidgetPorts();
    }

    private void stop() {
//        phidgetDevice.setConnected(false);
        opLogger.info("Stopping phidget controller");
        for (int i = 0; i < phidgetDevice.getPortSize(); i++) {
            final int port = i;
            digitalInputs.get(i).removeErrorListener(errorListenerList.get(i));
            if (logger.isDebugEnabled())
                logger.debug("Removed errorListener");
            digitalInputs.get(i).removeDetachListener(inputDetachListenerList.get(i));
            if (logger.isDebugEnabled())
                logger.debug("Removed detachListener");
            digitalInputs.get(i).removeAttachListener(inputAttachListenerList.get(i));
            if (logger.isDebugEnabled())
                logger.debug("Removed attachListener");
            digitalInputs.get(i).removeStateChangeListener(inputChangeListenerList.get(i));
            if (logger.isDebugEnabled())
                logger.debug("Removed inputChangeListener");
            try {
                digitalOutputs.get(i).close();
                logger.debug("digitalOutputs.get(" + i + ").close()");
                digitalInputs.get(i).close();
                logger.debug("digitalInputs.get(" + i + ").close()");
            } catch (final PhidgetException e) {
                logger.error("Exception thrown whilst closing interfaceKitPhidget: "
                        + e.getMessage());
            } finally {
                getPhidgetDevice().setConnectedInput(port, false);
                getPhidgetDevice().setConnectedOutput(port, false);
            }

            // Let the phidget port controllers die
            phidgetPortQueue.get(i).add(new ShutdownEvent());
        }
    }

    @Override
    public void run() {
        if (logger.isDebugEnabled())
            logger.debug("Starting phidget handler run()");
        start();
        if (logger.isDebugEnabled())
            logger.debug("PhidgetQueueHandler Started");

        while (true) {
            final Event event;
            try {
                if (logger.isDebugEnabled())
                    logger.debug("Try and take event from the phidget controller queue");
                event = eventQueue.take();
                if (logger.isDebugEnabled())
                    logger.debug("Taken event from the phidget queue");

                switch (event.getEventType()) {
                    case EVENT_CONFIGURE:
                        break;
                    case EVENT_SHUTDOWN:
                        stop();
                        return;
                    case EVENT_ACTION:
                        if (logger.isDebugEnabled())
                            logger.debug("Process EVENT_ACTION for HTTP");
                        if (!(event instanceof SendActionEvent)) {
                            if (logger.isDebugEnabled())
                                logger.debug("Wrong type of event for HTTP sending");
                            break;
                        }

                        if (logger.isDebugEnabled())
                            logger.debug("Looking good, cast action");
                        final SendActionEvent sendActionEvent = (SendActionEvent) event;
                        final Action action = sendActionEvent.getAction();

                        if (!(action instanceof PhidgetActionImpl))
                            break;

                        final PhidgetActionImpl phidgetActionImpl = (PhidgetActionImpl) action;
                        final int port = phidgetActionImpl.getPort();
                        if (port >= PhidgetConstants.PHIDGET_PORT_SIZE) {
                            if (logger.isDebugEnabled())
                                logger.debug("Invalid port specified (" + port
                                        + ")");
                            break;
                        }

                        phidgetPortQueue.get(port).add(event);

                        if (logger.isDebugEnabled())
                            logger.debug("All green for HTTP action");
                        break;
                }

            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
