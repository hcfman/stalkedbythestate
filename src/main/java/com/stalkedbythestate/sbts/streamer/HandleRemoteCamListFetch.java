package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.stalkedbythestate.sbts.eventlib.HttpAuthenticator;
import com.stalkedbythestate.sbts.json.CameraListJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

public class HandleRemoteCamListFetch implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(HandleRemoteCamListFetch.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private volatile RemoteFreakSpec remoteFreakSpec;
	private HttpAuthenticator authenticator;
	
	public HandleRemoteCamListFetch(RemoteFreakSpec remoteFreakSpec) {
		this.remoteFreakSpec = remoteFreakSpec;
	}

	@Override
	public void run() {
		handleFetch(remoteFreakSpec);
	}

	private void handleFetch(RemoteFreakSpec remoteFreakSpec) {
		CameraListJSON cameraListJSON = null;
		
		// Abort if there is no url
		FreakDevice freak = remoteFreakSpec.getFreakDevice();
		StringBuffer sb = new StringBuffer();
		sb.append(freak.getProtocol().toString().toLowerCase() + "://").append(freak.getHostname()).append(":").append(freak.getPort()).append("/sbts/");
		if (freak.isGuest())
			sb.append("guest/listcams");
		else
			sb.append("listcams");
			
		String urlString = sb.toString();
		if (logger.isDebugEnabled()) logger.debug("urlString: " + urlString);
		
		String username = freak.getUsername();
		String password = freak.getPassword();
		
		authenticator = HttpAuthenticator.getInstance();
		authenticator.setUsername(username);
		authenticator.setPassword(password);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Freak: " + freak.getName());
			logger.debug("urlString: " + urlString);
			logger.debug("username: " + username);
			logger.debug("password: " + password);
		}
		
		if (username != null && password != null && !username.trim().equals("")) {
			authenticator.setUsername(username);
			authenticator.setPassword(password);
//			if (logger.isDebugEnabled()) logger.debug("Setting an authenticator");
//			authenticator = new HttpAuthenticator(username, password);
//			Authenticator.setDefault(authenticator);
		}
		
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			if (logger.isDebugEnabled()) logger.debug("Malformed URL");
			return;
		}
		if (logger.isDebugEnabled()) logger.debug("URL looks good");

		// Fetch and junk data
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			if (logger.isDebugEnabled()) logger.debug("Try and get a connection");
			conn = (HttpURLConnection) url.openConnection();
			if (logger.isDebugEnabled()) logger.debug("Connection = " + conn);
			conn.setUseCaches(false);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
			
			try {
				conn.setRequestMethod("POST");
			} catch (ProtocolException e1) {
				opLogger.error("Protocol error fetching camera list");
				return;
			}
			
			// Send parameters
			String outputString = sb.toString();
			conn.setRequestProperty("Content-Length", "" + Integer.toString(outputString.getBytes().length));
			conn.setDoOutput(true);
			DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream ());
		    dataOutputStream.writeBytes(outputString);
			conn.getOutputStream().close();
			
			// Consume output
			if (logger.isDebugEnabled()) logger.debug("Now consume input");
			in = conn.getInputStream();
			sb.setLength(0);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = in.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, bytesRead));
			}
			if (logger.isDebugEnabled()) logger.debug("Finished reading all of the input");
			Gson gson = new Gson();
			if (logger.isDebugEnabled()) logger.debug("Total String: " + sb.toString());
			cameraListJSON = gson.fromJson(sb.toString(), CameraListJSON.class);
			if (logger.isDebugEnabled()) logger.debug("Got the json returning home I think");
		} catch (IOException e) {
			opLogger.error("Error fetching camera list from " + url.toString());
			cameraListJSON = new CameraListJSON(false);
			cameraListJSON.setMessages(Arrays.asList(new String[] {"I/O Exception"}));
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
		
		if (logger.isDebugEnabled()) logger.debug("Add the cameraListJSON to the queue (" + remoteFreakSpec.getQueue() + ") and return home");
		remoteFreakSpec.getQueue().add(cameraListJSON);
	}
}
