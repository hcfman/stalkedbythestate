package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import org.apache.log4j.Logger;

public class ValidityChecks {
	private static final Logger logger = Logger.getLogger(ValidityChecks.class);
	
	public static boolean isEventNameValid(String eventName) {
		if (logger.isDebugEnabled()) logger.debug("eventName = \"" + eventName + "\", regexp: " + "^[\\s0-9a-zA-Z_\\-\\.]*$");
		
		if (eventName.matches("^[\\s0-9a-zA-Z_\\-\\.]*$"))
			if (logger.isDebugEnabled()) logger.debug("It matches");
		else
			if (logger.isDebugEnabled()) logger.debug("It doesnt' match");
		return eventName.matches("^[\\s0-9a-zA-Z_\\-\\.]*$");
	}
	
	public static boolean isEventTimeValid(long eventTime) {
		return eventTime > 1301756132000L;
	}

}
