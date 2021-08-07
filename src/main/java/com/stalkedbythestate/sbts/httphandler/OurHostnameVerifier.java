package com.stalkedbythestate.sbts.httphandler;

import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class OurHostnameVerifier implements HostnameVerifier {
	private static final Logger logger = Logger.getLogger(OurHostnameVerifier.class);

	public boolean verify(String urlHostName, SSLSession sslSession) {
		if (logger.isDebugEnabled()) logger.debug("Warning: URL Host: " + urlHostName + " v/s " + sslSession.getPeerHost());

		if (logger.isDebugEnabled()) logger.debug("No verification needed");
		return true;
	}
}
