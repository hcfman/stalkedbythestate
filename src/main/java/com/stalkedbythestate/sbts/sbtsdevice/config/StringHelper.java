package com.stalkedbythestate.sbts.sbtsdevice.config;

public class StringHelper {
	
	synchronized public static String repeatChar(char c, int count) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count; i++)
			builder.append(c);
		return builder.toString();		
	}
}
