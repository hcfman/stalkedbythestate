package com.stalkedbythestate.sbts.sbtsdevice.config;

public enum TagActionType {
	SET_ON, SET_OFF;

	public static TagActionType set(String value) throws RuntimeException {
		if (value.equals("SET_ON"))
			return SET_ON;
		if (value.equals("SET_OFF"))
			return SET_OFF;

		throw new RuntimeException(
				"Invalid value specified for TagActionType Enum: " + value);
	}
}
