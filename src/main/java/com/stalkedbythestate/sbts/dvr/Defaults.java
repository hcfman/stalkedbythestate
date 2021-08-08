package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

public class Defaults {
	private int bufferSeconds = 6;
	private int bufferFramesPerSecond = 5;
	private int continueSeconds = 20;
	private int maxCameras = 10;
	private int numThreads = 1;
	
	public int getBufferSeconds() {
		return bufferSeconds;
	}
	public void setBufferSeconds(int bufferSeconds) {
		this.bufferSeconds = bufferSeconds;
	}
	public int getBufferFramesPerSecond() {
		return bufferFramesPerSecond;
	}
	public void setBufferFramesPerSecond(int bufferFramesPerSecond) {
		this.bufferFramesPerSecond = bufferFramesPerSecond;
	}
	public int getContinueSeconds() {
		return continueSeconds;
	}
	public void setContinueSeconds(int continueSeconds) {
		this.continueSeconds = continueSeconds;
	}
	public int getMaxCameras() {
		return maxCameras;
	}
	public void setMaxCameras(int maxCameras) {
		this.maxCameras = maxCameras;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
}
