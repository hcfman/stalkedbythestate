package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.WatchdogJSON;
import com.stalkedbythestate.sbts.json.WatchdogsJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@WebServlet(urlPatterns={"/watchdogsgetjson"})
public class WatchdogsGetJSON extends HttpServlet {
	private static final long serialVersionUID = 3157799115732191678L;
	private static final Logger logger = LoggerFactory
			.getLogger(WatchdogsGetJSON.class);
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

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		WatchdogsJSON watchdogsJSON = new WatchdogsJSON(true);
		Map<String, WatchdogJSON> watchdogMap = new HashMap<String, WatchdogJSON>();
		for (String eventName : sbtsConfig.getWatchdogConfig()
				.getTriggerMap().keySet()) {
			Watchdog watchdog = sbtsConfig.getWatchdogConfig()
					.getTriggerMap().get(eventName);

			WatchdogJSON watchdogJSON = new WatchdogJSON();
			watchdogJSON.setTriggerEventNames(watchdog
					.getTriggerEventNames());
			watchdogJSON.setResult(watchdog.getResult());
			watchdogJSON.setWithinSeconds(watchdog.getWithinSeconds());

			watchdogMap.put(eventName, watchdogJSON);
		}

		watchdogsJSON.setWatchdogMap(watchdogMap);
		SortedSet<String> eventSet = new TreeSet<String>();
		eventSet.addAll(sbtsConfig.getAvailableRfxcomEventNames());
		eventSet.addAll(sbtsConfig.getAvailableButtonEventNames());
		eventSet.addAll(sbtsConfig.getAvailablePhidgetEventnames());
		watchdogsJSON.setAvailableEventNames(eventSet);

		out.print(gson.toJson(watchdogsJSON));
	}

}
