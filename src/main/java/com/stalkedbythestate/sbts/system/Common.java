package com.stalkedbythestate.sbts.system;

import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

public class Common {
	public static final Logger logger = Logger.getLogger(Common.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private static final String SBTS_BASE = System.getenv("SBTS_HOME");
	private static volatile SbtsDeviceConfig sbtsConfig;

	static {
		if (logger.isDebugEnabled())
			logger.debug("In static initialization of Common");
		if (logger.isDebugEnabled())
			logger.debug("SBTS_BASE = " + SBTS_BASE);
	}

	private Common() {
		throw new RuntimeException("This class may not be instantiated");
	}
}
