package com.stalkedbythestate.sbts.diskwatchdog;

// Copyright (c) 2021 Kim Hendrikse

public class SpaceJSON {
	private long available;
	private long used;

	public SpaceJSON() {
	}

	public long getAvailable() {
		return available;
	}

	public void setAvailable(long available) {
		this.available = available;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	@Override
	public String toString() {
		return "SpaceJSON [available=" + available + ", used=" + used + "]";
	}

}
