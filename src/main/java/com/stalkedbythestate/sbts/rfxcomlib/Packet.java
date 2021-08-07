package com.stalkedbythestate.sbts.rfxcomlib;

import java.util.Arrays;

public class Packet {
	private int length;
	
	private int[] intArray;

	public Packet(byte[] bytes) {
		length = (bytes[0] + 256) % 256;
//		System.out.println("Construct packet of length: " + length);
		intArray = new int[length];
		
		for (int i = 1; i <= length; i++)
			intArray[i - 1] = ((bytes[i] + 256) % 256);
	}

	public Packet(byte[] bytes, int length) {
		this.length = length;
//		System.out.println("Construct packet of length: " + length);
		intArray = new int[length];
		
		for (int i = 0; i < length; i++)
			intArray[i] = ((bytes[i] + 256) % 256);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int[] getIntArray() {
		return intArray;
	}

	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	@Override
	public String toString() {
		return "Packet [length=" + length + ", intArray="
				+ Arrays.toString(intArray) + "]";
	}

}
