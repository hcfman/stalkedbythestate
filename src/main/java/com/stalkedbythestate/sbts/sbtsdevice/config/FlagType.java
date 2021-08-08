package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public enum FlagType {
	Always,
	Never,
	InConjunction;
	
	public static FlagType set(String value) throws RuntimeException {
		if (value.equals("Always"))
			return Always;
		if (value.equals("Never"))
			return Never;
		if (value.equals("InConjunction"))
			return InConjunction;		

		throw new RuntimeException(
				"Invalid value specified for Flagtype Enum: " + value);
	}
}
