package com.stalkedbythestate.sbts.httphandler;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class OurHostnameVerifier implements HostnameVerifier {
	private static final Logger logger = LoggerFactory.getLogger(OurHostnameVerifier.class);

	public boolean verify(String urlHostName, SSLSession sslSession) {
		if (logger.isDebugEnabled()) logger.debug("Warning: URL Host: " + urlHostName + " v/s " + sslSession.getPeerHost());

		if (logger.isDebugEnabled()) logger.debug("No verification needed");
		return true;
	}
}
