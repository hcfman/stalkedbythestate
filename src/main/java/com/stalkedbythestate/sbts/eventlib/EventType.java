package com.stalkedbythestate.sbts.eventlib;

public enum EventType {
	/*
	 * Internally generated
	 */
	EVENT_CONFIGURE, // Configure event
						// Resubscribe to combination events
	EVENT_CONFIGURE_COMBINATION_EVENTS, EVENT_CONFIGURE_WATCHDOG_EVENTS, EVENT_SHUTDOWN, // Initiate a shutdown
	EVENT_REBOOT, // Initiate a reboot
	EVENT_ACTION, // Tell a sub-system to perform an action, will contain an action object
	EVENT_PHONE_HOME, // Notify webserver of IP address

	/*
	 * Externally generated
	 */
	EVENT_HTTP_TRIGGER, EVENT_PHIDGET_IO_TRIGGER, EVENT_REMOTE_CAM_TRIGGER, EVENT_SCHEDULE_TRIGGER, EVENT_WATCHDOG_TRIGGER, EVENT_RFXCOM_TRIGGER, EVENT_SYNTHETIC_TRIGGER,

	VIDEO_TRIGGER, //

	SEND_MAIL, SEND_SMS, SEND_HTTP;

	public static String websocketName(EventType eventType) {
		String name = null;
		switch (eventType) {
		case EVENT_CONFIGURE_COMBINATION_EVENTS:
			name = "INTERNAL_CONFIGURE";
			break;
		case EVENT_SHUTDOWN:
			name = "INTERNAL_SHUTDOWN";
			break;
		case EVENT_REBOOT:
			name = "INTERNAL_REBOOT";
			break;
		case EVENT_ACTION:
			name = "INTERNAL_ACTION";
			break;
		case EVENT_PHONE_HOME:
			name = "INTERNAL_PHONE_HOME";
			break;
		case EVENT_HTTP_TRIGGER:
			name = "HTTP";
			break;
		case EVENT_PHIDGET_IO_TRIGGER:
			name = "PHIDGET";
			break;
		case EVENT_REMOTE_CAM_TRIGGER:
			name = "REMOTE_CAM";
			break;
		case EVENT_SCHEDULE_TRIGGER:
			name = "SCHEDULE";
			break;
		case EVENT_WATCHDOG_TRIGGER:
			name = "WATCHDOG";
			break;
		case EVENT_RFXCOM_TRIGGER:
			name = "RFXCOM";
			break;
		case EVENT_SYNTHETIC_TRIGGER:
			name = "COMBINATION_EVENT";
			break;
		case SEND_MAIL:
			name = "INTERNAL_MAIL";
			break;
		case SEND_HTTP:
			name = "INTERNAL_HTTP";
			break;
		case EVENT_CONFIGURE:
			break;
		case VIDEO_TRIGGER:
			break;
		default:
			break;
		}

		return name;
	}

}
