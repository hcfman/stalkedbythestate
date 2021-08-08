package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

public class PacketUtils {

	public String packetToString(int[] ints) {
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append("[");
		for (int i = 0; i < ints.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(String.format("%02X", ints[i]));
		}

		sb.append("]");
		return sb.toString();
	}

	public String packetToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		sb.append("[");
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0)
				sb.append(", ");
			int b = bytes[i] < 0 ? 256 + bytes[i] : bytes[i];
			sb.append(String.format("%02X", b));
		}

		sb.append("]");
		return sb.toString();
	}

}
