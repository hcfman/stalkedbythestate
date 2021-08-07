package com.stalkedbythestate.sbts.eh;

import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;

public interface WatchdogManager {

	void subscribe(Watchdog watchdog);

	void resetWatchdog(String eventName);
}
