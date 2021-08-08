package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

public class Request {
	static final int SAVE = 1;
	static final int QUIT = 2;
	
	int command;
	long eventTime;
	String description;

	Request( int command, long time, String description ) {
		this.command = command;
		
		eventTime = time;
		this.description = description;
	}

}
