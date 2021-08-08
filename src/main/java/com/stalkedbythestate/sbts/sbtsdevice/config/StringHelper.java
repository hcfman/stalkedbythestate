package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public class StringHelper {
	
	synchronized public static String repeatChar(char c, int count) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count; i++)
			builder.append(c);
		return builder.toString();		
	}
}
