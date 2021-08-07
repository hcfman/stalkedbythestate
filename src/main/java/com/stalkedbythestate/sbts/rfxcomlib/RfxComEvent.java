package com.stalkedbythestate.sbts.rfxcomlib;

public interface RfxComEvent {
	public String getEventName();

	public void setEventName(String eventName);

	public int[] getPacketInts();

	public void setPacketInts(int[] packetInts);

	public int[] getPacketMask();

	public void setPacketMask(int[] packetMask);

}
