package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.SyntheticJSON;
import com.stalkedbythestate.sbts.json.SyntheticsJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.config.SynthTrigger;
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

@WebServlet(urlPatterns={"/syntheticsgetjson"})
public class SyntheticsGetJSON extends HttpServlet {
	private static final long serialVersionUID = 4849780100322337750L;
	private static final Logger logger = LoggerFactory
			.getLogger(SyntheticsGetJSON.class);
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

		SyntheticsJSON syntheticsJSON = new SyntheticsJSON(true);
		Map<String, SyntheticJSON> syntheticMap = new HashMap<String, SyntheticJSON>();
		for (String eventName : sbtsConfig.getSynthTriggerConfig()
				.getTriggerMap().keySet()) {
			SynthTrigger synthTrigger = sbtsConfig.getSynthTriggerConfig()
					.getTriggerMap().get(eventName);

			SyntheticJSON syntheticJSON = new SyntheticJSON();
			syntheticJSON.setTriggerEventNames(synthTrigger
					.getTriggerEventNames());
			syntheticJSON.setResult(synthTrigger.getResult());
			syntheticJSON.setWithinSeconds(synthTrigger.getWithinSeconds());

			syntheticMap.put(eventName, syntheticJSON);
		}

		syntheticsJSON.setSyntheticMap(syntheticMap);
		SortedSet<String> eventSet = new TreeSet<String>();
		eventSet.addAll(sbtsConfig.getAvailableRfxcomEventNames());
		eventSet.addAll(sbtsConfig.getAvailableButtonEventNames());
		eventSet.addAll(sbtsConfig.getAvailablePhidgetEventnames());
		syntheticsJSON.setAvailableEventNames(eventSet);

		out.print(gson.toJson(syntheticsJSON));
	}

}
