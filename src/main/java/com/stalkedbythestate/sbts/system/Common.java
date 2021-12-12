package com.stalkedbythestate.sbts.system;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
	public static final Logger logger = LoggerFactory.getLogger(Common.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
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
