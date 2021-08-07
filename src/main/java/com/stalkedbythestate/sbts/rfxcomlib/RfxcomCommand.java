package com.stalkedbythestate.sbts.rfxcomlib;

import java.util.Arrays;

public class RfxcomCommand {
	private String name;
	private String description;
	private RfxcomType rfxcomType;
	private String eventName;
	private int hysteresis;
	private byte[] packet;
	private int[] packetValues1;
	private int[] packetValues2;
	private int[] mask;
	private RfxcomOperator[] operator;

	public RfxcomCommand(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public RfxcomCommand(String name, String description, RfxcomType rfxcomType) {
		this.name = name;
		this.description = description;
		this.rfxcomType = rfxcomType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RfxcomType getRfxcomType() {
		return rfxcomType;
	}

	public void setRfxcomType(RfxcomType rfxcomType) {
		this.rfxcomType = rfxcomType;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getHysteresis() {
		return hysteresis;
	}

	public void setHysteresis(int hysteresis) {
		this.hysteresis = hysteresis;
	}

	public byte[] getPacket() {
		return packet;
	}

	public void setPacket(byte[] packet) {
		this.packet = packet;
	}

	public int[] getPacketValues1() {
		return packetValues1;
	}

	public void setPacketValues1(int[] packetValues1) {
		this.packetValues1 = packetValues1;
	}

	public int[] getPacketValues2() {
		return packetValues2;
	}

	public void setPacketValues2(int[] packetValues2) {
		this.packetValues2 = packetValues2;
	}

	public int[] getMask() {
		return mask;
	}

	public void setMask(int[] mask) {
		this.mask = mask;
	}

	public RfxcomOperator[] getOperator() {
		return operator;
	}

	public void setOperator(RfxcomOperator[] operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "RfxcomCommand [name=" + name + ", description=" + description
				+ ", rfxcomType=" + rfxcomType + ", eventName=" + eventName
				+ ", hysteresis=" + hysteresis + ", packet="
				+ Arrays.toString(packet) + ", packetValues1="
				+ Arrays.toString(packetValues1) + ", packetValues2="
				+ Arrays.toString(packetValues2) + ", mask="
				+ Arrays.toString(mask) + ", operator="
				+ Arrays.toString(operator) + "]";
	}

}
