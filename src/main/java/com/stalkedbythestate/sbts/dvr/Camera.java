package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

public class Camera {
	private int index;
	private String url;
	private String username;
	private String password;
	private int priority = 1;
	private int bufferSeconds = -1;
	private int bufferFramesPerSecond = -1;
	private int continueSeconds = -1;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getBufferSeconds() {
		return bufferSeconds;
	}

	public int getBufferSeconds( int defaultValue ) {
		return bufferSeconds == -1 ? defaultValue : bufferSeconds;
	}

	public void setBufferSeconds(int bufferSeconds) {
		this.bufferSeconds = bufferSeconds;
	}

	public int getBufferFramesPerSecond() {
		return bufferFramesPerSecond;
	}

	public int getBufferFramesPerSecond( int defaultValue ) {
		return bufferFramesPerSecond == -1 ? defaultValue : bufferFramesPerSecond;
	}

	public void setBufferFramesPerSecond(int bufferFramesPerSecond) {
		this.bufferFramesPerSecond = bufferFramesPerSecond;
	}

	public int getContinueSeconds() {
		return continueSeconds;
	}

	public int getContinueSeconds( int defaultValue ) {
		return continueSeconds == -1 ? defaultValue : continueSeconds;
	}

	public void setContinueSeconds(int continueSeconds) {
		this.continueSeconds = continueSeconds;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Camera() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
