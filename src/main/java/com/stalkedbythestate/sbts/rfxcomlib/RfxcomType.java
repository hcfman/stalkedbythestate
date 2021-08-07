package com.stalkedbythestate.sbts.rfxcomlib;

public enum RfxcomType {
	GENERIC_INPUT, GENERIC_OUTPUT;

	public static RfxcomType set(String value) throws RuntimeException {
		if (value.equals("GENERIC_INPUT"))
			return GENERIC_INPUT;
		if (value.equals("GENERIC_OUTPUT"))
			return GENERIC_OUTPUT;
		throw new RuntimeException(
				"Invalid value specified for RfxcomType Enum: " + value);
	}

}
