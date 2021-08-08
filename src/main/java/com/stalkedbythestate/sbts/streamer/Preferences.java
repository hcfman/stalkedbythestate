package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.eventlib.ConfigureVideoEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.PreferencesJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/preferences"})
public class Preferences extends HttpServlet {
	private static final long serialVersionUID = -4525671991027438195L;
	private static final Logger logger = Logger.getLogger(Preferences.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");
		if (logger.isDebugEnabled())
			logger.debug("In Preferences saving");

		sbtsConfig = freak.getSbtsConfig();

		response.setContentType("application/json");

		if (logger.isDebugEnabled())
			logger.debug("content-type: " + request.getHeader("content-type"));

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		String body = stringBuilder.toString();

		if (logger.isDebugEnabled())
			logger.debug("Body: " + body);

		Gson fromGson = new Gson();
		PreferencesJSON preferencesJSON = fromGson.fromJson(body,
				PreferencesJSON.class);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("PreferencesJSON has become: "
					+ gson.toJson(preferencesJSON));

		sbtsConfig.getSettingsConfig().setWebPrefix(
				preferencesJSON.getWebPrefix());

		int oldConnectTimeout = sbtsConfig.getSettingsConfig()
				.getConnectTimeout();
		int newConnectTimeout = preferencesJSON.getConnectTimeout();
		sbtsConfig.getSettingsConfig().setConnectTimeout(
				preferencesJSON.getConnectTimeout());

		if (oldConnectTimeout != newConnectTimeout) {
			if (logger.isDebugEnabled())
				logger.debug("oldConnectTimeout: " + oldConnectTimeout
						+ ", newConnectTimeout: " + newConnectTimeout);
			freak.sendEvent(new ConfigureVideoEvent());
		}

		sbtsConfig.getSettingsConfig().setFreeSpace(
				preferencesJSON.getFreeSpace());
		sbtsConfig.getSettingsConfig().setCleanRate(
				preferencesJSON.getCleanRate());
		sbtsConfig.getSettingsConfig()
				.setDaysMJPG(preferencesJSON.getDaysJpeg());
		sbtsConfig.getSettingsConfig().setPhoneHome(
				preferencesJSON.getPhonehomeUrl());

		ResultMessage resultMessage = new ResultMessage(true, "");

		if (freak.getUpdating().get()) {
			resultMessage.setResult(false);
			resultMessage.getMessages().add(
					"Can't save preferences, an update is in progress");
			opLogger.info("Can't save preferences, an update is in progress");
		} else {
			// Now save to disk
			freak.saveConfig();
			opLogger.info("Updated preferences");
		}

		PrintWriter out = response.getWriter();
		out.print(gson.toJson(resultMessage));

	}

}
