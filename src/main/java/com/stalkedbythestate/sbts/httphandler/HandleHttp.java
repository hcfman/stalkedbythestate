package com.stalkedbythestate.sbts.httphandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.ContainsPacket;
import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.HttpAuthenticator;
import com.stalkedbythestate.sbts.eventlib.HttpTriggerEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.HttpActionImpl;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Map;

public class HandleHttp implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HandleHttp.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private Event event;
	private Event originalEvent;
	private Action action;
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	public HandleHttp(FreakApi freak, Event event, Event originalEvent,
                      Action action) {
		this.freak = freak;
		this.event = event;
		this.originalEvent = originalEvent;
		this.action = action;
	}

	@Override
	public void run() {
		sbtsConfig = freak.getSbtsConfig();

		if (logger.isDebugEnabled())
			logger.debug("Have the sbts config");

		HttpActionImpl httpAction = (HttpActionImpl) action;

		// Abort if there is no url
		String urlString = httpAction.getUrl();
		if (logger.isDebugEnabled())
			logger.debug("urlString: " + urlString);
		if (urlString == null || urlString.trim().equals("")) {
			if (logger.isDebugEnabled())
				logger.debug("Empty url, returning");
			return;
		}
		urlString = urlString.trim();

		// Check for bad protocol
		if (!urlString.matches("^(?i)http(?:s)?://.*$")) {
			if (logger.isDebugEnabled())
				logger.debug("Doesn't match the right protocol, returning:"
						+ urlString);
			return;
		}

		HttpAuthenticator authenticator = HttpAuthenticator.getInstance();
		String username = httpAction.getUsername();
		String password = httpAction.getPassword();

		if (username != null && password != null && !username.trim().equals("")) {
			authenticator.setUsername(username);
			authenticator.setPassword(password);
			if (logger.isDebugEnabled())
				logger.debug("Setting an authenticator");
		}

		MethodType methodType = httpAction.getMethodType();
		// Check if need to add parameters to the url (Only on GET)
		StringBuffer sb = new StringBuffer();
		Map<String, String> parameters = httpAction.getParameters();
		if (parameters != null && parameters.size() > 0) {
			if (logger.isDebugEnabled())
				logger.debug("Need to set some parameters for a "
						+ httpAction.getMethodType().toString());
			if (methodType == MethodType.GET) {
				if (logger.isDebugEnabled())
					logger.debug("Append to the original url");
				sb.append(urlString);
				sb.append("?");
			}
			int count = 0;
			for (String key : parameters.keySet()) {
				if (count > 0)
					sb.append("&");
				sb.append(key);
				sb.append("=");
				try {
					String processedParameter = parameters.get(key).replaceAll(
							"\\$\\{eventTime\\}",
							Long.toString(event.getEventTime()));
					if (originalEvent instanceof HttpTriggerEvent) {
						HttpTriggerEvent httpTriggerEvent = (HttpTriggerEvent) originalEvent;
						processedParameter = processedParameter.replaceAll(
								"\\$\\{clientEventTime\\}", Long
										.toString(httpTriggerEvent
												.getClientEventTime()));
					}
					if (originalEvent instanceof ContainsPacket) {
						processedParameter = processedParameter.replaceAll(
								"\\$\\{packet\\}",
								((ContainsPacket) originalEvent)
										.getPacketString());
					}
					sb.append(URLEncoder.encode(processedParameter, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("Unsupported encoding exception: "
							+ e.getMessage());
					return;
				}
				count++;
			}
			if (methodType == MethodType.GET)
				urlString = sb.toString();
		}

		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			if (logger.isDebugEnabled())
				logger.debug("Malformed URL");
			return;
		}
		if (logger.isDebugEnabled())
			logger.debug("URL looks good, protocol: " + url.getProtocol());

		boolean isHttps = "https".equalsIgnoreCase(url.getProtocol());

		// Fetch and junk data
		HttpURLConnection httpConn = null;
		HttpsURLConnection httpsConn = null;
		URLConnection conn = null;
		InputStream in = null;
		try {
			if (logger.isDebugEnabled())
				logger.debug("Try and get a connection");
			conn = url.openConnection();

			if (isHttps) {
				httpsConn = (HttpsURLConnection) conn;
				if (!httpAction.isVerifyHostname()) {
					if (logger.isDebugEnabled())
						logger.debug("Skipping hostname verification");
					httpsConn.setHostnameVerifier(new OurHostnameVerifier());
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Verifying hostname");
				}
			} else
				httpConn = (HttpURLConnection) conn;

			if (logger.isDebugEnabled())
				logger.debug("Connection = " + conn);
			conn.setUseCaches(false);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			try {
				if (isHttps)
					httpsConn.setRequestMethod(httpAction.getMethodType()
							.toString());
				else
					httpConn.setRequestMethod(httpAction.getMethodType()
							.toString());
			} catch (ProtocolException e1) {
				opLogger.error("Protocol error sending http: "
						+ e1.getMessage());
				return;
			}

			// Send parameters
			if (methodType == MethodType.POST) {
				String outputString = sb.toString();
				conn.setRequestProperty("Content-Length",
						"" + Integer.toString(outputString.getBytes().length));
				conn.setDoOutput(true);
				DataOutputStream dataOutputStream = new DataOutputStream(
						conn.getOutputStream());
				dataOutputStream.writeBytes(outputString);
				conn.getOutputStream().close();
			}

			// Consume and toss input
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			while (conn.getInputStream().read(buffer) > 0) {
			}
			if (logger.isDebugEnabled())
				logger.debug("Input consumed");
		} catch (IOException e) {
			opLogger.error("Input/Output error sending to " + urlString + ": " + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("HTTP event Sent.....");
	}
}
