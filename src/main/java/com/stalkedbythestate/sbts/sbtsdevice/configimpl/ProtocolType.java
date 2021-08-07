package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

public enum ProtocolType {
	HTTP, HTTPS;

	public static ProtocolType set(String value) throws RuntimeException {
		if (value.equals("HTTP"))
			return HTTP;
		if (value.equals("HTTPS"))
			return HTTPS;

		throw new RuntimeException(
				"Invalid value specified for Protocol Enum: " + value);
	}
}
