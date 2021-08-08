package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.concurrent.LinkedBlockingQueue;

public class DiskConfig {
	private volatile DiskState diskState = DiskState.NO_DISK;
	private volatile String lastMessage = "";
	private volatile Thread thread;
	private volatile LinkedBlockingQueue<Progress> progressQueue;

	public DiskState getDiskState() {
		return diskState;
	}

	public void setDiskState(DiskState diskState) {
		this.diskState = diskState;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public LinkedBlockingQueue<Progress> getProgressQueue() {
		return progressQueue;
	}

	public void setProgressQueue(LinkedBlockingQueue<Progress> progressQueue) {
		this.progressQueue = progressQueue;
	}

	@Override
	public String toString() {
		return "DiskConfig [diskState=" + diskState + ", lastMessage="
				+ lastMessage + ", progressQueue=" + progressQueue
				+ ", thread=" + thread + "]";
	}
	
}
