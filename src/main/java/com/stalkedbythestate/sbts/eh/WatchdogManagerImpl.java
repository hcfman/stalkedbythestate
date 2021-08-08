package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class WatchdogManagerImpl implements WatchdogManager {
	private static final Logger logger = Logger.getLogger(WatchdogManagerImpl.class);
    private final Map<String, Set<Watchdog>> eventMap = new HashMap<>();
	private final EventHandler eventHandler;

	public WatchdogManagerImpl(final EventHandler eventHandler) {
		super();
		this.eventHandler = eventHandler;
	}

	public void subscribe(final Watchdog watchdog) {
		for (final String eventName : watchdog.getTriggerEventNames()) {
			Set<Watchdog> hashedSet = eventMap.get(eventName);
			if (hashedSet == null)
				hashedSet = new LinkedHashSet<>();

			logger.debug("Subscribe watchdog (" + watchdog.getResult() + ") to: " + eventName);
			hashedSet.add(watchdog);
			eventMap.put(eventName, hashedSet);
		}

		if (logger.isDebugEnabled())
			logger.debug("watchdog subscribe(" + watchdog + ")");
	}

	public void resetWatchdog(final String triggerEventName) {
		if (eventMap.containsKey(triggerEventName)) {
            for (final Watchdog watchdog : eventMap.get(triggerEventName)) {
                eventHandler.scheduleWatchdogTimer(watchdog.getResult(), watchdog.getWithinSeconds());
            }
        }
	}

}
