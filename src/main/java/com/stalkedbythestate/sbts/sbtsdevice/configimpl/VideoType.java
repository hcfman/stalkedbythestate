package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

public enum VideoType {
	MJPEG, WEBM, PJPEG;

	public static VideoType set(String value) throws RuntimeException {
		if (value.equals("MJPEG"))
			return MJPEG;
		if (value.equals("WEBM"))
			return WEBM;
		if (value.equals("PJPEG"))
			return PJPEG;
		throw new RuntimeException(
				"Invalid value specified for VideoType Enum: " + value);
	}
}
