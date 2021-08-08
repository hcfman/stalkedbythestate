package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public enum PhidgetActionType {
	On,
	Off,
	Pulse;
	
	public static PhidgetActionType set(String value) throws RuntimeException {
		if (value.equals("On"))
			return On;
		if (value.equals("Off"))
			return Off;
		if (value.equals("Pulse"))
			return Pulse;
		
		throw new RuntimeException(
				"Invalid value specified for PhidgetActionType Enum: " + value);
	}

}
