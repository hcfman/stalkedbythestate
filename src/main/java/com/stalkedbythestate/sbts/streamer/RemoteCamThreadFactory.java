package com.stalkedbythestate.sbts.streamer;

import java.util.concurrent.ThreadFactory;

public class RemoteCamThreadFactory implements ThreadFactory {
	private static int count = 0;
	
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setName("remoteCamListFetch-" + ++count);
		return thread;
	}

}
