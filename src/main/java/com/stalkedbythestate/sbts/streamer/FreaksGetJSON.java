package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.FreakJSON;
import com.stalkedbythestate.sbts.json.FreaksJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
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

@WebServlet(urlPatterns={"/freaksgetjson"})
public class FreaksGetJSON extends HttpServlet {
	private static final long serialVersionUID = 3003922593335993839L;
	private static final Logger logger = LoggerFactory.getLogger(FreaksGetJSON.class);
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

		FreaksJSON freaksJSON = new FreaksJSON(true);

		for (FreakDevice freakDevice : sbtsConfig.getFreakConfig().getFreakMap()
				.values()) {

			FreakJSON freakJSON = new FreakJSON();
			freakJSON.setName(freakDevice.getName());
			freakJSON.setDescription(freakDevice.getDescription());
			freakJSON.setHostname(freakDevice.getHostname());
			freakJSON.setPort(freakDevice.getPort());
			freakJSON.setProtocol(freakDevice.getProtocol());
			freakJSON.setVerifyHostname(freakDevice.isVerifyHostname());
			freakJSON.setUsername(freakDevice.getUsername());
			freakJSON.setPassword(freakDevice.getPassword());
			freakJSON.setGuest(freakDevice.isGuest());

			freaksJSON.getFreaks().add(freakJSON);
		}

		out.print(gson.toJson(freaksJSON));
	}

}
