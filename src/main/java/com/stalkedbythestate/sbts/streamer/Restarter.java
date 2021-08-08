package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;

import java.io.IOException;

public class Restarter {
	private FreakApi freak;

	public Restarter(FreakApi freak) {
		this.freak = freak;
	}

	public static void restart(FreakApi freak) throws IOException {
		Runtime.getRuntime().exec(freak.getSbtsBase() + "/bin/restart.sh");
	}

	public static void reboot(FreakApi freak) throws IOException {
		Runtime.getRuntime().exec(freak.getSbtsBase() + "/bin/sbts_reboot");
	}

	public static void shutdown(FreakApi freak) throws IOException {
		Runtime.getRuntime().exec(freak.getSbtsBase() + "/bin/sbts_shutdown");
	}

}
