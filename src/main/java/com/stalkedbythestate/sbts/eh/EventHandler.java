package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.*;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.*;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandler {
	private static final Logger logger = Logger.getLogger(EventHandler.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private static final Logger actionLogger = Logger.getLogger("action");
	private final FreakApi freak;
	private final LinkedBlockingQueue<Event> ehEventQueue;
	private volatile Boolean ready = false;
	private final SbtsDeviceConfig sbtsConfig;
	private volatile SyntheticEventManager syntheticEventManager;
	private volatile WatchdogManager watchdogManager;
	private final Map<String, Boolean> staticTagMap = new ConcurrentHashMap<>();
	private final Map<String, Boolean> transientTagMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Timer> transientTagTasks = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Timer> delayedActions = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Long> hysteresisMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Timer> scheduleMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Timer> watchdogMap = new ConcurrentHashMap<>();

	public EventHandler(final FreakApi freak, final LinkedBlockingQueue<Event> ehEventQueue) {
		this.freak = freak;
		this.ehEventQueue = ehEventQueue;

		sbtsConfig = freak.getSbtsConfig();
	}

	private class FireSchedule extends TimerTask {
		private final Timer timer;
		private final Schedule schedule;
		private final int dow;

		FireSchedule(final Timer timer, final Schedule schedule, final int dow) {
			this.timer = timer;
			this.schedule = schedule;
			this.dow = dow;
		}

		@Override
		public void run() {
			scheduleMap.remove(schedule.getName() + "-" + dow);
			timer.cancel();
			if (logger.isDebugEnabled())
				logger.debug("Firing schedule: " + schedule.getName() + "-" + dow + " for event: "
						+ schedule.getEventName());

			if (logger.isDebugEnabled())
				logger.debug("Delivering Schedule trigger event");
			freak.sendEvent(new ScheduleTriggerEvent(schedule.getEventName(), System.currentTimeMillis()));

			// Re-schedule
			if (logger.isDebugEnabled())
				logger.debug("Re-schedule");
			scheduleFor(schedule, dow);
		}

	}

	private class FireWatchdog extends TimerTask {
		private final Timer timer;
		private final String eventName;
		private final long seconds;

		FireWatchdog(final Timer timer, final String eventName, final long seconds) {
			this.timer = timer;
			this.eventName = eventName;
			this.seconds = seconds;
		}

		@Override
		public void run() {
			watchdogMap.remove(eventName);
			timer.cancel();
			if (logger.isDebugEnabled())
				logger.debug("Firing watchdog timer for event: " + eventName);

			if (logger.isDebugEnabled())
				logger.debug("Delivering Schedule trigger event");
			freak.sendEvent(new WatchdogEvent(eventName, System.currentTimeMillis()));

			// Re-schedule
			if (logger.isDebugEnabled())
				logger.debug("Re-schedule watchdog");
			scheduleWatchdogTimer(eventName, seconds);
		}

	}

	private void scheduleFor(final Schedule schedule, final int dow) {
		final String scheduleName = schedule.getName();
		final String eventName = schedule.getEventName();
		if (logger.isDebugEnabled())
			logger.debug("Starting schedules for day: " + dow + "(Java " + dow + 1 + ")");
		final Calendar cal = Calendar.getInstance();
		final int nowDow = cal.get(Calendar.DAY_OF_WEEK);

		// How many days away is this schedule?
		final int daysDifference;
		if (nowDow > dow) {
			daysDifference = 7 - nowDow + dow;
			if (logger.isDebugEnabled())
				logger.debug("nowDow (" + nowDow + ") > dow (" + dow + ")");
		} else {
			daysDifference = dow - nowDow;
			if (logger.isDebugEnabled())
				logger.debug("nowDow (" + nowDow + ") <= dow (" + dow + ")");
		}
		if (logger.isDebugEnabled())
			logger.debug("daysDifference: " + daysDifference);

		if (logger.isDebugEnabled())
			logger.debug("Start hour: " + schedule.getTimeSpec().getTimes().get(0).getStartHour());
		cal.add(Calendar.DATE, daysDifference);
		cal.set(Calendar.HOUR_OF_DAY, schedule.getTimeSpec().getTimes().get(0).getStartHour());
		cal.set(Calendar.SECOND, schedule.getTimeSpec().getTimes().get(0).getStartSec());
		cal.set(Calendar.MINUTE, schedule.getTimeSpec().getTimes().get(0).getStartMin());
		cal.set(Calendar.MILLISECOND, 0);
		if (logger.isDebugEnabled())
			logger.debug("Calculated millis for schedule: " + cal.getTimeInMillis());
		if (logger.isDebugEnabled())
			logger.debug("Now is millis: " + System.currentTimeMillis());

		if (cal.getTimeInMillis() - 5 < System.currentTimeMillis()) {
			// In the past, add 7 days

			if (logger.isDebugEnabled())
				logger.debug("Need to add days");
			cal.set(Calendar.HOUR_OF_DAY, 12);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE, 7);
			cal.set(Calendar.HOUR_OF_DAY, schedule.getTimeSpec().getTimes().get(0).getStartHour());
			cal.set(Calendar.SECOND, schedule.getTimeSpec().getTimes().get(0).getStartSec());
			cal.set(Calendar.MINUTE, schedule.getTimeSpec().getTimes().get(0).getStartMin());
			cal.set(Calendar.MILLISECOND, 0);
		}

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
		if (logger.isDebugEnabled())
			logger.debug("Schedule for: " + sdf.format(cal.getTime()));

		final Timer timer = new Timer();
		final Timer oldTimer = scheduleMap.get(scheduleName + "-" + dow);

		// Renew any other transients that may be active
		if (oldTimer != null) {
			if (logger.isDebugEnabled())
				logger.debug("Old timer found(" + scheduleName + "-" + dow + "), resetting");
			oldTimer.cancel();
		}
		if (logger.isDebugEnabled())
			logger.debug("Adding schedule: " + scheduleName + "-" + dow + " for eventName: " + eventName + " for "
					+ cal.getTime().toString());
		scheduleMap.put(scheduleName + "-" + dow, timer);
		timer.schedule(new FireSchedule(timer, schedule, dow), cal.getTime());
	}

	void scheduleWatchdogTimer(final String eventName, final long seconds) {
		final Timer timer = new Timer();
		final Timer oldTimer = watchdogMap.get(eventName);

		// Renew any other transients that may be active
		if (oldTimer != null) {
			if (logger.isDebugEnabled())
				logger.debug("Old watchdog timer found(" + eventName + "), resetting");
			oldTimer.cancel();
		}
		if (logger.isDebugEnabled())
			logger.debug("Adding watchdog timer: " + eventName + " for " + Long.toString(seconds) + " seconds");
		watchdogMap.put(eventName, timer);
		timer.schedule(new FireWatchdog(timer, eventName, seconds), seconds * 1000);
	}

	private void spawnSchedule(final Schedule schedule) {
		if (logger.isDebugEnabled())
			logger.debug("Starting schedules: " + schedule);
		for (final int dow : schedule.getTimeSpec().getDows()) {
			if (logger.isDebugEnabled())
				logger.debug("Dow is: " + dow);
			scheduleFor(schedule, dow);
		}
	}

	private void startSchedules() {
		if (logger.isDebugEnabled())
			logger.debug("Check for schedules");
		for (final Schedule schedule : sbtsConfig.getScheduleConfig().getSchedules()) {
			if (logger.isDebugEnabled())
				logger.debug("Found a schedule, starting");
			spawnSchedule(schedule);
		}
	}

	private void startWatchdogTimers() {
		final Map<String, Watchdog> triggerMap = sbtsConfig.getWatchdogConfig().getTriggerMap();

		if (logger.isDebugEnabled())
			logger.debug("Starting watchdog times ");
		for (final String watchdogEvent : triggerMap.keySet()) {
			final long seconds = triggerMap.get(watchdogEvent).getWithinSeconds();
			if (logger.isDebugEnabled())
				logger.debug("Found a watchdog(" + watchdogEvent + ", " + Long.toString(seconds) + "), starting");
			scheduleWatchdogTimer(watchdogEvent, seconds);
		}
	}

	private void restartSchedules() {
		// Cancel all existing schedules
		if (logger.isDebugEnabled())
			logger.debug("Cancel existing schedules");
		for (final String scheduleKey : scheduleMap.keySet()) {
			scheduleMap.get(scheduleKey).cancel();
			scheduleMap.remove(scheduleKey);
		}

		// Now restart them
		if (logger.isDebugEnabled())
			logger.debug("Restart schedules");
		startSchedules();
	}

	private void restartWatchdogs() {
		// Cancel all existing schedules
		if (logger.isDebugEnabled())
			logger.debug("Cancel existing watchdogs");
		for (final String watchdogKey : watchdogMap.keySet()) {
			watchdogMap.get(watchdogKey).cancel();
			watchdogMap.remove(watchdogKey);
		}

		// Now restart them
		if (logger.isDebugEnabled())
			logger.debug("Restart watchdogs");
		startWatchdogTimers();
		resubscribeToWatchdogEvents();
	}

	private void resubscribeToCombinationEvents() {
		final SyntheticEventManagerImpl newSyntheticEventManager = new SyntheticEventManagerImpl();
		final EventListener eventListener = new EventListenerImpl(freak);
		final SynthTriggerConfig synthTriggerConfig = sbtsConfig.getSynthTriggerConfig();

		if (synthTriggerConfig == null) {
			syntheticEventManager = newSyntheticEventManager;
			return;
		}

		for (final SynthTrigger synthTrigger : synthTriggerConfig.getTriggerMap().values()) {
			newSyntheticEventManager.subscribe(eventListener, synthTrigger.getWithinSeconds() * 1000,
					synthTrigger.getResult(), synthTrigger.getTriggerEventNames().toArray(new String[0]));
		}

		syntheticEventManager = newSyntheticEventManager;
	}

	private void resubscribeToWatchdogEvents() {
		final WatchdogManagerImpl newWatchdogManager = new WatchdogManagerImpl(this);
		final WatchdogConfig watchdogConfig = sbtsConfig.getWatchdogConfig();

		if (watchdogConfig == null) {
			watchdogManager = newWatchdogManager;
			return;
		}

		for (final Watchdog watchdog : watchdogConfig.getTriggerMap().values()) {
			newWatchdogManager.subscribe(watchdog);
		}

		watchdogManager = newWatchdogManager;
	}

	public final void start() {
		resubscribeToCombinationEvents();
		logger.info("EVENT HANDLER STARTUP");

		if (logger.isDebugEnabled())
			logger.debug("About to handle");
		handle();
		if (logger.isDebugEnabled())
			logger.debug("Started handle, start schedules");
		startSchedules();
		startWatchdogTimers();
		resubscribeToWatchdogEvents();
	}

	private class PullTag extends TimerTask {
		private final Timer timer;
		private final String actionName;
		private final String tagName;

		PullTag(final Timer timer, final String actionName, final String tagName) {
			this.timer = timer;
			this.actionName = actionName;
			this.tagName = tagName;
		}

		@Override
		public void run() {
			transientTagMap.remove(tagName);
			transientTagTasks.remove(actionName);
			timer.cancel();
			if (logger.isDebugEnabled())
				logger.debug("Pulling tag: " + tagName + " for action: " + actionName);
			if (logger.isDebugEnabled())
				logger.debug("Transient tags are: " + transientTagMap.toString());
		}

	}

	private class DelayedAction extends TimerTask {
		private final Timer timer;
		private final String actionName;
		private final Event event;
		private final Action action;

		DelayedAction(final Timer timer, final String actionName, final Event event, final Action action) {
			this.timer = timer;
			this.actionName = actionName;
			this.event = event;
			this.action = action;
		}

		@Override
		public void run() {
			delayedActions.remove(actionName);
			timer.cancel();
			if (logger.isDebugEnabled())
				logger.debug("Pulling action: " + actionName + " at " + System.currentTimeMillis());
			if (logger.isDebugEnabled())
				logger.debug("delayedActions are: " + delayedActions.toString());
			executeAction(event, action, System.currentTimeMillis(), true);
		}

	}

	/*
	 * This carries out the intended action. All of the requirements have been met
	 */
	private void executeAction(final Event event, final Action action, final long eventTime, final boolean reExecute) {
		final TagActionImpl tagActionImpl;
		final String actionName = action.getName();
		final int delayedFor = action.getDelayFor();

		if (logger.isDebugEnabled())
			logger.debug("Executing action: " + action.getActionType());

		if (!reExecute && delayedFor > 0) {
			final UnitType delayUnits = action.getDelayUnits();
			final Timer timer = new Timer();
			final Timer oldTimer = delayedActions.get(actionName);

			// Renew any other transients that may be active
			if (oldTimer != null) {
				if (logger.isDebugEnabled())
					logger.debug("Old timer found(" + actionName + "), resetting");
				oldTimer.cancel();
			}
			if (logger.isDebugEnabled())
				logger.debug(
						"Delaying (" + delayedFor + (delayUnits != null && (delayUnits == UnitType.ms) ? "ms" : "sec")
								+ ") for action: " + actionName + " at " + System.currentTimeMillis());
			delayedActions.put(actionName, timer);
			timer.schedule(new DelayedAction(timer, actionName, event, action),
					(delayUnits != null && (delayUnits == UnitType.ms) ? (delayedFor) : (delayedFor * 1000)));

			actionLogger.info(action.getName() + ": Delayed");

			return;
		}

		actionLogger.info(action.getName() + ": Fired");

		switch (action.getActionType()) {
		case ACTION_EMAIL:
			freak.sendEmailEvent(new SendActionEvent(action, event, eventTime));
			break;
		case ACTION_ADD_TAG:
			tagActionImpl = (TagActionImpl) action;
			final Long validFor = tagActionImpl.getValidFor();
			if (validFor != null && validFor != 0L) {
				final String tagName = tagActionImpl.getTagName();

				final Timer timer = new Timer();
				final Timer oldTimer = transientTagTasks.get(actionName);

				// Renew any other transients that may be active
				if (oldTimer != null) {
					if (logger.isDebugEnabled())
						logger.debug("Old timer found(" + actionName + "), resetting");
					oldTimer.cancel();
				}
				if (logger.isDebugEnabled())
					logger.debug("Adding transient tag: " + tagName + " for action: " + actionName);
				transientTagMap.put(tagName, true);
				transientTagTasks.put(actionName, timer);
				timer.schedule(new PullTag(timer, actionName, tagName), validFor * 1000);

				if (logger.isDebugEnabled())
					logger.debug("transientTagTasks set: " + transientTagTasks.keySet().toString());
			} else {
				staticTagMap.put(tagActionImpl.getTagName(), true);
				if (logger.isDebugEnabled())
					logger.debug("Static set: " + staticTagMap.toString());
			}

			break;
		case ACTION_DELETE_TAG:
			tagActionImpl = (TagActionImpl) action;
			final String tagName = tagActionImpl.getTagName();

			// Remove static tags
			staticTagMap.remove(tagName);

			// Also remove any transient tags
			transientTagMap.remove(tagName);
			final Timer timer = transientTagTasks.get(actionName);
			if (timer != null) {
				timer.cancel();
				transientTagTasks.remove(actionName);
			}
			break;
		case ACTION_VIDEO:
			freak.sendDvrEvent(new SendActionEvent(action, event, eventTime));
			break;
		case ACTION_SEND_HTTP:
			freak.sendHttpEvent(new SendActionEvent(action, event, eventTime));
			break;
		case ACTION_REMOTE_VIDEO:
			final RemoteVideoActionImpl remoteVideoActionImpl = (RemoteVideoActionImpl) action;
			final String freakName = remoteVideoActionImpl.getFreakName();
			final FreakDevice freakDevice = sbtsConfig.getFreakConfig().getFreakMap().get(freakName);
			if (freakDevice == null) {
				logger.error("Freak not found: " + freakName);
				break;
			}

			final StringBuilder sb = new StringBuilder();
			if (logger.isDebugEnabled())
				logger.debug("Freak protocol is: " + freakDevice.getProtocol());
			sb.append(freakDevice.getProtocol().toString().toLowerCase()).append("://").append(freakDevice.getHostname())
					.append(":").append(freakDevice.getPort())
					.append(freakDevice.isGuest() ? "/sbts/guest/remotecam" : "/sbts/remotecam");
			final HttpActionImpl httpActionImpl = new HttpActionImpl(action.getName(), action.getEventName(),
					action.getDescription(), sb.toString(), MethodType.GET, freakDevice.isVerifyHostname(),
					freakDevice.getUsername(), freakDevice.getPassword());

			if (logger.isDebugEnabled())
				logger.debug("Setting eventName: " + action.getEventName());
			httpActionImpl.getParameters().put("eventTime", Long.toString(event.getEventTime()));
			httpActionImpl.getParameters().put("eventName", action.getEventName());
			httpActionImpl.getParameters().put("description", action.getDescription());

			final StringBuilder cameraListBuffer = new StringBuilder();

			for (final int i : remoteVideoActionImpl.getCameraSet()) {
				if (cameraListBuffer.length() > 0)
					cameraListBuffer.append(",");
				cameraListBuffer.append(Integer.toString(i));
			}

			httpActionImpl.getParameters().put("cameralist", cameraListBuffer.toString());

			freak.sendHttpEvent(new SendActionEvent(httpActionImpl, event, eventTime));
			break;
		case ACTION_CANCEL_ACTION:
			if (logger.isDebugEnabled())
				logger.debug("Got cancel action action");
			final CancelActionActionImpl cancelActionActionImpl = (CancelActionActionImpl) action;
			final Timer cancelActionTimer = delayedActions.get(cancelActionActionImpl.getActionName());
			if (cancelActionTimer != null) {
				if (logger.isDebugEnabled())
					logger.debug("Cancelling action with name (" + cancelActionActionImpl.getActionName() + ")");
				cancelActionTimer.cancel();
				delayedActions.remove(cancelActionActionImpl.getActionName());
			}
			break;
		case ACTION_WEB_PREFIX:
			final WebPrefixActionImpl webPrefixActionImpl = (WebPrefixActionImpl) action;
			sbtsConfig.getSettingsConfig().setWebPrefix(webPrefixActionImpl.getPrefix());

			// Now save to disk (If not updating)
			freak.saveConfig();
			break;
		case ACTION_PHIDGET_OUTPUT:
			freak.sendPhidgetEvent(new SendActionEvent(action, event, eventTime));
			break;
		case ACTION_RFXCOM:
			freak.sendRfxcomEvent(new SendActionEvent(action, event, eventTime));
			break;
		}
	}

	/*
	 * Check if the requirements meet, if so call execute
	 */
	private void handleAction(final Event event, final Action action, final long eventTime, final Set<String> currentTagSet) {
		if (logger.isDebugEnabled())
			logger.debug("Check if meet all of the requirements");
		// If the action has a TimeSpec list then this must be active for the
		// action to be processed
		final Collection<TimeSpec> timeSpecList = action.getValidTimes();

		if (timeSpecList != null && timeSpecList.size() > 0) {
			if (logger.isDebugEnabled())
				logger.debug("There is a timespec for this action, check if active");
			boolean validTime = false;
			for (final TimeSpec timeSpec : timeSpecList)
				if (timeSpec.within(eventTime)) {
					if (logger.isDebugEnabled())
						logger.debug("it is active");
					validTime = true;
					break;
				}
			if (!validTime) {
				if (logger.isDebugEnabled())
					logger.debug("It is not valid");
				actionLogger.info(action.getName() + ": Skipped");
				return;
			}
		}

		// This action is processed if all the positive and negative constraints
		// are ok

		/*
		 * First the positive constraints
		 */
		if (logger.isDebugEnabled())
			logger.debug("Check positive tagnames");
		final Set<String> positiveTagNames = action.getPositiveTagNames();
		boolean willExecute = true;
		if (positiveTagNames != null && positiveTagNames.size() > 0) {
			if (action.isPositiveTagAndMode()) {
				for (final String tagName : positiveTagNames) {
					if (logger.isDebugEnabled())
						logger.debug("Check (" + tagName + ") against(" + currentTagSet + ")");
					if (!currentTagSet.contains(tagName)) {
						if (logger.isDebugEnabled())
							logger.debug("Matching positive tag: " + tagName);
						willExecute = false;
						break;
					}
				}
			} else {
				willExecute = false;
				for (final String tagName : positiveTagNames) {
					if (logger.isDebugEnabled())
						logger.debug("Check (" + tagName + ") against(" + currentTagSet + ")");
					if (currentTagSet.contains(tagName)) {
						if (logger.isDebugEnabled())
							logger.debug("Matching positive tag: " + tagName);
						willExecute = true;
						break;
					}
				}
			}
		}

		if (!willExecute) {
			if (logger.isDebugEnabled())
				logger.debug("No required positive tags match, aborting");
			actionLogger.info(action.getName() + ": Skipped");
			return;
		}

		/*
		 * Now check if any are present that are not allowed
		 */

		final Set<String> negativeTagNames = action.getNegativeTagNames();

		if (logger.isDebugEnabled())
			logger.debug("Now check the negative tagNames");
		if (negativeTagNames != null && negativeTagNames.size() > 0) {
			for (final String tagName : negativeTagNames) {
				if (logger.isDebugEnabled())
					logger.debug("Check (" + tagName + ") against(" + currentTagSet + ")");
				if (currentTagSet.contains(tagName)) {
					if (logger.isDebugEnabled())
						logger.debug("Matching negative tag, aborting: " + tagName);
					actionLogger.info(action.getName() + ": Skipped");
					return;
				}
			}
		}

		// Check if there is a counter and if so, if it can fire
		final EventCounter eventCounter = action.getEventcounter();
		if (eventCounter != null && !eventCounter.shouldFire()) {
			actionLogger.info(action.getName() + ": Skipped");
			return;
		}

		// Check if can fire at this time, i.e. the correct amount of time has elapsed since last time
		final UnitType hysteresisUnits = action.getHysteresisUnits();
		final int hysteresis = (hysteresisUnits != null && hysteresisUnits == UnitType.ms) ? (action.getHysteresis())
				: (action.getHysteresis() * 1000);

		boolean canExecute = false;
		if (hysteresis == 0) {
			canExecute = true;
		} else {
			final long now = System.currentTimeMillis();
			final Long lastTime = hysteresisMap.get(action.getName());
			if (lastTime == null || now - lastTime > hysteresis) {
				hysteresisMap.put(action.getName(), now);
				canExecute = true;
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Hysteresis active, ignore event (" + action.getEventName() + ") for action "
							+ action.getName());
			}
		}

		// Already fired the allowable times within the allowable limit
		if (!canExecute) {
			actionLogger.info(action.getName() + ": Skipped");
			return;
		}

		executeAction(event, action, eventTime, false);
	}

	private boolean isProfileActive(final Profile profile, final long eventTime) {
//		if (logger.isDebugEnabled())
//			logger.debug("Checking profile if within valid time: " + profile.getTagName());
		final Collection<TimeSpec> timeSpecList = profile.getValidTimes();

		if (timeSpecList == null) {
			if (logger.isDebugEnabled())
				logger.debug("TimeSpec list for profile (" + profile.getTagName()
						+ ") is null, returning true (Always active)");
			return true;
		}

		for (final TimeSpec timeSpec : timeSpecList) {
//			if (logger.isDebugEnabled())
//				logger.debug("Iterating over profile");
			if (timeSpec.within(eventTime)) {
				if (logger.isDebugEnabled())
					logger.debug("It's within time (" + profile.getTagName() + ")");
				return true;
			}
		}

//		if (logger.isDebugEnabled())
//			logger.debug("Not within valid time: " + profile.getTagName());
		return false;
	}

	private void handleActions(final boolean guest, final Event event, final String eventName, final long eventTime) {
		// The final set of tags that are present at this time
		final Set<String> currentTagSet = new HashSet<>();

		// Add current active tags to the list
		final ProfileConfig profileConfig = sbtsConfig.getProfileConfig();
		if (profileConfig != null) {
			final SortedSet<Profile> profileList = profileConfig.getProfileList();
			if (profileList != null) {
				for (final Profile profile : profileList)
					if (isProfileActive(profile, eventTime)) {
//						if (logger.isDebugEnabled())
//							logger.debug("Profile (" + profile.getTagName() + ") is now active");
						currentTagSet.add(profile.getTagName());
					}
			}
		}

		// Static tags are either set or reset manually
		currentTagSet.addAll(staticTagMap.keySet());
		if (logger.isDebugEnabled())
			logger.debug("staticTags are: " + staticTagMap.toString());

		// Transient tags may or may not be set, but they only last a fixed
		// period of time
		currentTagSet.addAll(transientTagMap.keySet());
		if (logger.isDebugEnabled())
			logger.debug("Transient tags are: " + transientTagMap.toString());

		// Now we have a set of tags which are present, evaluate the positive
		// remaining constraints
		// for each action
		final ActionConfig actionConfig = sbtsConfig.getActionConfig();
		if (logger.isDebugEnabled())
			logger.debug("Now checkout the actions, compare eventName against (" + eventName + "), actionList size: "
					+ actionConfig.getActionList().size());
		for (final Action action : actionConfig.getActionList()) {
//			if (logger.isDebugEnabled())
//				logger.debug("action.eventName: " + action.getEventName());
			// Action matches eventName, set if can fire
			if (action.getEventName().equals(eventName)) {
				if (logger.isDebugEnabled())
					logger.debug("Action MATCH (" + action.getEventName() + "), check constraints");
				if (action.getProfiles().size() > 0) {
					if (logger.isDebugEnabled())
						logger.debug("Action has profiles, at least one must match");
					boolean found = false;
					for (final String tagName : action.getProfiles()) {
						if (logger.isDebugEnabled())
							logger.debug("Check if the profile (" + tagName + ") is active now");
						if (tagName != null && currentTagSet.contains(tagName)) {
							found = true;
							if (logger.isDebugEnabled())
								logger.debug("tagName (" + tagName + ") was found to be active, we can continue");
							break;
						}
					}

					// Action has profiles, there is no match, don't execute
					if (!found) {
						if (logger.isDebugEnabled())
							logger.debug(
									"No active profile found to match for current action: " + action + ", skipping");
						actionLogger.info(action.getName() + ": Skipped");
						continue;
					}
				}

				if (!guest || action.isGuest()) {
					handleAction(event, action, eventTime, currentTagSet);
				} else {
					opLogger.info("Skipping action (" + action.getName() + ") as not permissioned for guest access");
					actionLogger.info(action.getName() + ": Skipped");
				}
			}
		}
	}

	private boolean maySendEvent(final boolean guest, final String eventName) {
		for (final List<HttpTrigger> httpTriggers : sbtsConfig.getHttpConfig().getGroupMap().values()) {
			for (final HttpTrigger httpTrigger : httpTriggers) {
				if (eventName.equals(httpTrigger.getEventName())) {
					if (guest) {
						if (!httpTrigger.isGuest()) {
							opLogger.info(
									"Attempt to fire event \"" + eventName + "\" : Button doesn't allow guest access");
							return false;
						} else
							return true;
					} else {
						return true;
					}
				}
			}
		}

		opLogger.info("Attempt to fire event \"" + eventName + "\" : There is no button defined for this event");
		return false;
	}

	private void handle() {
		final Thread thread = new Thread() {
			private final Map<com.stalkedbythestate.sbts.eventlib.EventListener, Pattern> eventListenerMap = freak.getListenerMap();

			@Override
			public void run() {
                ready = true;

				while (true) {
					final Event event;
					try {
						if (logger.isDebugEnabled())
							logger.debug("Waiting to take an event");
						event = ehEventQueue.take();
						if (logger.isDebugEnabled())
							logger.debug("event-handler event: " + event.getEventType());

						switch (event.getEventType()) {
						case EVENT_CONFIGURE:
							if (event instanceof ConfigureRfxcomEvent)
								freak.sendRfxcomEvent(event);
							else if (event instanceof ConfigureVideoEvent)
								freak.sendDvrEvent(event);
							else if (event instanceof ConfigureScheduleEvent)
								restartSchedules();
							else if (event instanceof ConfigureWatchdogEvent)
								restartWatchdogs();
							break;
						case EVENT_CONFIGURE_COMBINATION_EVENTS:
							opLogger.info("Update combination events");
							resubscribeToCombinationEvents();
							break;
						case EVENT_CONFIGURE_WATCHDOG_EVENTS:
							break;
						case EVENT_SHUTDOWN:
							freak.sendDvrEvent(event);
							return;
						case EVENT_REBOOT:
							break;
						case EVENT_HTTP_TRIGGER:
							final HttpTriggerEvent httpEvent = (HttpTriggerEvent) event;
							syntheticEventManager.fireSynthetics(httpEvent.getEventName());
							watchdogManager.resetWatchdog(httpEvent.getEventName());
							if (maySendEvent(httpEvent.isGuest(), httpEvent.getEventName())) {
								handleActions(httpEvent.isGuest(), event, httpEvent.getEventName(),
										httpEvent.getEventTime());
							}
							break;
						case EVENT_PHIDGET_IO_TRIGGER:
							final PhidgetTriggerEvent phidgetEvent = (PhidgetTriggerEvent) event;
							syntheticEventManager.fireSynthetics(phidgetEvent.getEventName());
							watchdogManager.resetWatchdog(phidgetEvent.getEventName());
							handleActions(false, event, phidgetEvent.getEventName(), phidgetEvent.getEventTime());
							break;
						case EVENT_RFXCOM_TRIGGER:
							final RfxcomTriggerEvent rfxcomEvent = (RfxcomTriggerEvent) event;
							syntheticEventManager.fireSynthetics(rfxcomEvent.getEventName());
							watchdogManager.resetWatchdog(rfxcomEvent.getEventName());
							handleActions(false, event, rfxcomEvent.getEventName(), rfxcomEvent.getEventTime());
							break;
						case EVENT_SYNTHETIC_TRIGGER:
							final SyntheticTriggerEvent syntheticTriggerEvent = (SyntheticTriggerEvent) event;
							syntheticEventManager.fireSynthetics(syntheticTriggerEvent.getEventName());
							watchdogManager.resetWatchdog(syntheticTriggerEvent.getEventName());
							handleActions(false, event, syntheticTriggerEvent.getEventName(),
									syntheticTriggerEvent.getEventTime());
							break;
						case EVENT_REMOTE_CAM_TRIGGER:
							final RemoteCamTriggerEvent remoteCamTriggerEvent = (RemoteCamTriggerEvent) event;

							final String actionName = remoteCamTriggerEvent.getEventName();
							final String actionEventName = remoteCamTriggerEvent.getEventName();
							final String actionDescription = remoteCamTriggerEvent.getDescription();

							final VideoActionImpl videoActionImpl = new VideoActionImpl(actionName, actionEventName,
									actionDescription, remoteCamTriggerEvent.getCameraSet());

							// Send a video event for this trigger
							freak.sendDvrEvent(
									new SendActionEvent(videoActionImpl, event, remoteCamTriggerEvent.getEventTime()));

							// Now inject an http event. This is optional for
							// the host to execute
							if (maySendEvent(remoteCamTriggerEvent.isGuest(), remoteCamTriggerEvent.getEventName())) {
								freak.sendEvent(new HttpTriggerEvent(remoteCamTriggerEvent.getEventName(),
										remoteCamTriggerEvent.getEventTime(), remoteCamTriggerEvent.getEventTime(),
										remoteCamTriggerEvent.isGuest()));
							}
							break;
						case EVENT_SCHEDULE_TRIGGER:
							final ScheduleTriggerEvent scheduleEvent = (ScheduleTriggerEvent) event;
							syntheticEventManager.fireSynthetics(scheduleEvent.getEventName());
							watchdogManager.resetWatchdog(scheduleEvent.getEventName());
							handleActions(false, event, scheduleEvent.getEventName(), scheduleEvent.getEventTime());
							break;
						case EVENT_WATCHDOG_TRIGGER:
							final WatchdogEvent watchdogEvent = (WatchdogEvent) event;
							syntheticEventManager.fireSynthetics(watchdogEvent.getEventName());
							handleActions(false, event, watchdogEvent.getEventName(), watchdogEvent.getEventTime());
							break;
						case EVENT_ACTION:
							break;
						case EVENT_PHONE_HOME:
							break;
						case SEND_HTTP:
							break;
						case SEND_MAIL:
							break;
						case VIDEO_TRIGGER:
							break;
						default:
							break;
						}

						// Call back any listeners
						for (final com.stalkedbythestate.sbts.eventlib.EventListener eventListener : eventListenerMap.keySet()) {
							final Pattern p = eventListenerMap.get(eventListener);
							if (event instanceof Nameable) {
								final Matcher m = p.matcher(((Nameable) event).getEventName());
								if (m.find())
									eventListener.onEvent(new Notification((Nameable) event));
							}
						}

					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};

		thread.setName("event-handler");
		thread.start();
	}

	public boolean isReady() {
		final boolean result;
        result = ready;
		return result;
	}

	public List<String> getTagList() {
		final Set<String> tagSet = new HashSet<>();

		tagSet.addAll(staticTagMap.keySet());
		tagSet.addAll(transientTagMap.keySet());

		return Arrays.asList(tagSet.toArray(new String[0]));
	}

	public void clearTags(final List<String> tagList) {
		for (final String tagName : tagList) {
			staticTagMap.remove(tagName);
			transientTagMap.remove(tagName);
		}
	}
}
