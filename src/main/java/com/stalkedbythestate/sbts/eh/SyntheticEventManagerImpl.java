package com.stalkedbythestate.sbts.eh;

import com.stalkedbythestate.sbts.eventlib.SyntheticTriggerEvent;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SyntheticEventManagerImpl implements SyntheticEventManager {
	private static final Logger logger = Logger.getLogger(SyntheticEventManagerImpl.class);
	private Map<String, Set<Trigger>> eventMap = new HashMap<String, Set<Trigger>>();
	private Map<String, Long> lastTriggered = new HashMap<String, Long>();

	// class Trigger extends LinkedHashSet<String> {
	class Trigger {
		private EventListener listener = null;
		private long timeRange;
		private String resultEventName;
		private Set<String> triggerSet = new LinkedHashSet<String>();

		public Trigger(EventListener listener, long timeRange, String eventName, String... eventStrings)
				throws Exception {
			if (listener == null)
				throw new Exception("You must define a listener");
			this.listener = listener;
			this.timeRange = timeRange;
			this.resultEventName = eventName;

			for (String s : eventStrings)
				triggerSet.add(s);
		}

		boolean shouldFire(String s, long now) {
			if (logger.isDebugEnabled())
				logger.debug("In shouldFire(" + s + ")");
			if (logger.isDebugEnabled())
				logger.debug("now: " + now);
			lastTriggered.put(s, now);
			if (logger.isDebugEnabled())
				logger.debug("lastFiredMap.put(" + s + ", " + now + ");");
			for (String key : triggerSet) {
				if (logger.isDebugEnabled())
					logger.debug("Iterating over this ?? : key : " + key);

				Long lastFired = lastTriggered.get(key);

				if (logger.isDebugEnabled())
					logger.debug("  => Check(" + key + "), lastFired = " + lastFired + ", timeRange = " + timeRange);

				if (!key.equals(s) && (lastFired == null || now - lastFired > timeRange)) {
					if (logger.isDebugEnabled())
						logger.debug("Expired, returning");
					return false;
				}
			}
			if (logger.isDebugEnabled())
				logger.debug("Got all events, shouldFire = true");

			return true;
		}

		public String getResultEventName() {
			return resultEventName;
		}

		public EventListener getListener() {
			return listener;
		}

		public String toString() {
			return "Trigger [resultEventName=" + resultEventName + ", " + "timeRange=" + timeRange + ", set="
					+ super.toString() + "]";
		}
	}

	public void subscribe(EventListener listener, long timeRange, String resultEventName, String... eventDescriptions) {
		if (logger.isDebugEnabled())
			logger.debug(
					"setSynthetic(" + timeRange + ", resultEventName = " + resultEventName + ", eventDescriptions = ");
		for (String s : eventDescriptions)
			if (logger.isDebugEnabled())
				logger.debug("event desc:" + s);

		Trigger trigger;
		try {
			trigger = new Trigger(listener, timeRange, resultEventName, eventDescriptions);
		} catch (Exception e) {
			logger.error("Empty listener provided to subscribe: ", e);
			return;
		}
		for (String s : eventDescriptions) {
			Set<Trigger> hashedSet = eventMap.get(s);
			if (hashedSet == null)
				hashedSet = new LinkedHashSet<Trigger>();

			hashedSet.add(trigger);

			if (logger.isDebugEnabled())
				logger.debug("Setting synthetic eventMap with key " + s);

			eventMap.put(s, hashedSet);
		}
	}

	public void fireSynthetics(String eventName) {
		Set<Trigger> triggerSet;

		// Get list of items that can be completed and triggered by this event
		if ((triggerSet = eventMap.get(eventName)) == null) {
			if (logger.isDebugEnabled())
				logger.debug("Nothing to trigger on");
			return;
		}

		long now = System.currentTimeMillis();
		for (Trigger trigger : triggerSet) {
			if (logger.isDebugEnabled())
				logger.debug("Checking trigger: " + trigger);

			if (trigger.shouldFire(eventName, now))
				trigger.getListener()
						.onEvent(new SyntheticTriggerEvent(trigger.getResultEventName(), System.currentTimeMillis()));
		}
	}

}
