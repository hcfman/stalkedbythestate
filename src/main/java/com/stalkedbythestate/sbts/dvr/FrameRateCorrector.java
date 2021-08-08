package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class FrameRateCorrector {
	private CircularFifoBuffer ringBuffer = new CircularFifoBuffer();
	private String cam;

	public boolean add(String cam, long timeValue) {
		this.cam = cam;
		long limit = timeValue - 1000;
		int length;
		while ((length = ringBuffer.size()) > 0
				&& (Long) ringBuffer.get() < limit)
			ringBuffer.remove();

		// Don't add if would exceed 6 fps
		if (length >=6)
			return false;

		ringBuffer.add(timeValue);

		return true;
	}

}
