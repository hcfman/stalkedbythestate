package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.rfxcomlib.RfxComEvent;

import java.util.Arrays;

public class RfxComEventImpl implements RfxComEvent {
	private String eventName;
	private int[] packetInts;
	private int[] packetMask;

	public RfxComEventImpl(String eventName, int[] packetInts, int[] packetMask) {
		this.eventName = eventName;
		this.packetInts = packetInts;
		this.packetMask = packetMask;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int[] getPacketInts() {
		return packetInts;
	}

	public void setPacketInts(int[] packetInts) {
		this.packetInts = packetInts;
	}

	public int[] getPacketMask() {
		return packetMask;
	}

	public void setPacketMask(int[] packetMask) {
		this.packetMask = packetMask;
	}

	@Override
	public String toString() {
		return "RfxComEventImpl [eventName=" + eventName + ", packetInts="
				+ Arrays.toString(packetInts) + ", packetMask="
				+ Arrays.toString(packetMask) + "]";
	}

}
