package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.eventlib.HttpAuthenticator;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ViewJSON;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import java.util.Set;

public class HandleRemoteCamFetch implements Runnable {
	private final static Logger logger = Logger
			.getLogger(HandleRemoteCamFetch.class);
	private final SbtsDeviceConfig sbtsConfig;
	private final VideoType videoType;
	private final RemoteCameraSpec remoteCameraSpec;
	private HttpAuthenticator authenticator;
	private FreakApi freak;

	public HandleRemoteCamFetch(final SbtsDeviceConfig sbtsConfig, final VideoType videoType,
								final RemoteCameraSpec remoteCameraSpec) {
		this.sbtsConfig = sbtsConfig;
		this.videoType = videoType;
		this.remoteCameraSpec = remoteCameraSpec;
	}

	@Override
	public void run() {
		freak = Freak.getInstance();
		handleFetch(remoteCameraSpec);
	}

	private void handleFetch(final RemoteCameraSpec remoteCameraSpec) {
		ViewJSON viewJSON = null;

		// Abort if there is no url
		final FreakDevice freakDevice = sbtsConfig.getFreakConfig().getFreakMap()
				.get(remoteCameraSpec.getFreakName());
		final StringBuilder sb = new StringBuilder();
		sb.append(freakDevice.getProtocol().toString().toLowerCase()).append("://")
				.append(freakDevice.getHostname()).append(":")
				.append(freakDevice.getPort()).append("/sbts/");
		if (freakDevice.isGuest())
			sb.append("guest/");
		if (videoType == VideoType.WEBM)
			sb.append("remotewebmjson");
		else
			sb.append("remotemjpgjson");

		final String urlString = sb.toString();
		if (logger.isDebugEnabled())
			logger.debug("urlString: " + urlString);

		final HttpAuthenticator authenticator = HttpAuthenticator.getInstance();
		final String username = freakDevice.getUsername();
		final String password = freakDevice.getPassword();

		if (username != null && password != null && !username.trim().equals("")) {
			authenticator.setUsername(username);
			authenticator.setPassword(password);
		}

		// Check if need to add parameters to the url (Only on GET)
		sb.setLength(0);

		// Add cameralist
		final Set<Integer> cameraSet = remoteCameraSpec.getCameraSet();
		int count = 0;
		for (final int camIndex : cameraSet) {
			if (logger.isDebugEnabled())
				logger.debug("camIndex: " + camIndex);
			if (count == 0) {
				sb.append("cameralist=").append(camIndex);
			} else {
				sb.append(",").append(camIndex);
			}
			count++;
		}

		if (count > 0)
			sb.append("&");

		// Add eventlist
		final List<String> eventFilterList = remoteCameraSpec.getEventFilterList();
		count = 0;
		if (eventFilterList != null) {
			for (final String eventName : eventFilterList) {
				if (logger.isDebugEnabled())
					logger.debug("eventName: " + eventName);
				try {
					if (count == 0) {
						sb.append("eventlist=").append(
								URLEncoder.encode(eventName, "UTF-8"));
					} else {
						sb.append(",").append(
								URLEncoder.encode(eventName, "UTF-8"));
					}
					count++;
				} catch (final UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("Error encoding URL eventName");
				}
			}
			if (count > 0)
				sb.append("&");
		}

		sb.append("startdate=").append(remoteCameraSpec.getStartDateStr())
				.append("&enddate=").append(remoteCameraSpec.getEndDateStr());
		final String times = remoteCameraSpec.getTimes();
		if (times != null)
			sb.append("&timerange=").append(times);
		if (logger.isDebugEnabled())
			logger.debug("parameters: " + sb.toString());

		URL url;
		try {
			url = new URL(urlString);
		} catch (final MalformedURLException e) {
			if (logger.isDebugEnabled())
				logger.debug("Malformed URL");
			return;
		}
		if (logger.isDebugEnabled())
			logger.debug("URL looks good");

		// Fetch and junk data
		HttpURLConnection conn;
		InputStream in = null;
		try {
			if (logger.isDebugEnabled())
				logger.debug("Try and get a connection");
			conn = (HttpURLConnection) url.openConnection();
			if (logger.isDebugEnabled())
				logger.debug("Connection = " + conn);
			conn.setUseCaches(false);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			try {
				conn.setRequestMethod("POST");
			} catch (final ProtocolException e1) {
				e1.printStackTrace();
				return;
			}

			// Send parameters
			final String outputString = sb.toString();
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(outputString.getBytes().length));
			conn.setDoOutput(true);
			final DataOutputStream dataOutputStream = new DataOutputStream(
					conn.getOutputStream());
			dataOutputStream.writeBytes(outputString);
			conn.getOutputStream().close();

			// Consume output
			if (logger.isDebugEnabled())
				logger.debug("Now consume input");
			in = conn.getInputStream();
			sb.setLength(0);
			final byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = conn.getInputStream().read(buffer)) > 0) {
				// System.out.println("Got: " + new String(buffer, 0,
				// bytesRead));
				sb.append(new String(buffer, 0, bytesRead));
			}
			final Gson gson = new Gson();
			if (logger.isDebugEnabled())
				logger.debug("Total String: " + sb.toString());
			viewJSON = gson.fromJson(sb.toString(), ViewJSON.class);
		} catch (final IOException e) {
			if (logger.isDebugEnabled())
				logger.debug("Got exception");
			e.printStackTrace();
			remoteCameraSpec.getQueue().add(
					new ViewJSON(freak, remoteCameraSpec.getCameraSet()
							.size(), remoteCameraSpec.getCameraSet(),
							remoteCameraSpec.getEventFilterList()));
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (final IOException ignored) {
			}
		}

		remoteCameraSpec.getQueue().add(viewJSON);
	}
}
