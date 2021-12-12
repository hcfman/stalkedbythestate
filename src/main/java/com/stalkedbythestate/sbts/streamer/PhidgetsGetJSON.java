package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.PhidgetJSON;
import com.stalkedbythestate.sbts.json.PhidgetsJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
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

@WebServlet(urlPatterns={"/phidgetsgetjson"})
public class PhidgetsGetJSON extends HttpServlet {
	private static final long serialVersionUID = 1751117016948122490L;
	private static final Logger logger = LoggerFactory
			.getLogger(PhidgetsGetJSON.class);
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

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		PhidgetsJSON phidgetsJSON = new PhidgetsJSON(true);
		Map<String, PhidgetJSON> phidgetMap = new HashMap<String, PhidgetJSON>();
		for (String phidgetName : sbtsConfig.getPhidgetConfig().getPhidgetMap()
				.keySet()) {
			PhidgetDevice phidgetDevice = sbtsConfig.getPhidgetConfig()
					.getPhidgetMap().get(phidgetName);

			PhidgetJSON phidgetJSON = new PhidgetJSON();
			phidgetJSON.setName(phidgetName);
			phidgetJSON.setSerialNumber(Integer.toString(phidgetDevice
					.getSerialNumber()));
			phidgetJSON.setPortSize(Integer.toString(phidgetDevice
					.getPortSize()));
			phidgetJSON.setName(phidgetDevice.getName());
			phidgetJSON.setDescription(phidgetDevice.getDescription());
			phidgetJSON.setInitialInputState(phidgetDevice
					.getInitialInputState());
			phidgetJSON.setInitialOutputState(phidgetDevice
					.getInitialOutputState());
			phidgetJSON.setOffTriggerEventNames(phidgetDevice
					.getOffTriggerEventNames());
			phidgetJSON.setOnTriggerEventNames(phidgetDevice
					.getOnTriggerEventNames());

			phidgetMap.put(phidgetName, phidgetJSON);
		}

		phidgetsJSON.setPhidgetMap(phidgetMap);

		out.print(gson.toJson(phidgetsJSON));
	}

}
