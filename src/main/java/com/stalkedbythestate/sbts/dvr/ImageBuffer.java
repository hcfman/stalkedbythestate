package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

public class ImageBuffer {
	private long timestamp;
	private byte buffer[];
	private int bufferSize;
	
	void setTimestamp( long timestamp ) {
		this.timestamp = timestamp;
	}
	
	long getTimestamp() {
		return timestamp;
	}
	
	int size() {
		return bufferSize;
	}
	
	byte[] getBuffer() {
		return buffer;
	}
	
	ImageBuffer( long timestamp, byte buffer[], int size ) {
		this.timestamp = timestamp;
		this.buffer = buffer;
		bufferSize = size;
	}

}
