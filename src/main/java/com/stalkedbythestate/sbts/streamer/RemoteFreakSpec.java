package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import com.stalkedbythestate.sbts.json.CameraListJSON;

import java.util.concurrent.LinkedBlockingQueue;

public class RemoteFreakSpec {
	private FreakDevice freakDevice;
	private LinkedBlockingQueue<CameraListJSON> queue = new LinkedBlockingQueue<CameraListJSON>();

	public RemoteFreakSpec() {
	}

	public RemoteFreakSpec(FreakDevice freakDevice) {
		this.freakDevice = freakDevice;
	}

	public FreakDevice getFreakDevice() {
		return freakDevice;
	}

	public void setFreakDevice(FreakDevice freakDevice) {
		this.freakDevice = freakDevice;
	}

	public LinkedBlockingQueue<CameraListJSON> getQueue() {
		return queue;
	}

	public void setQueue(LinkedBlockingQueue<CameraListJSON> queue) {
		this.queue = queue;
	}

	@Override
	public String toString() {
		return "RemoteFreakSpec [freakDevice=" + freakDevice + ", queue="
				+ queue + "]";
	}
	
}
