package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

public enum MethodType {
	GET, POST;

	public static MethodType set(String value) throws RuntimeException {
		if (value.equals("GET"))
			return GET;
		if (value.equals("POST"))
			return POST;

		throw new RuntimeException(
				"Invalid value specified for MethodType Enum: " + value);
	}
}
