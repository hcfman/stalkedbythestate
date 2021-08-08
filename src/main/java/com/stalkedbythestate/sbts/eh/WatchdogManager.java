package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;

public interface WatchdogManager {

	void subscribe(Watchdog watchdog);

	void resetWatchdog(String eventName);
}
