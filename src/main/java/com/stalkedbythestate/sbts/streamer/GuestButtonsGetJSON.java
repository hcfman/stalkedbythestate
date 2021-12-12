package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ButtonEventsJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.HttpTrigger;
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
import java.util.List;

@WebServlet(urlPatterns={"/guest/guestbuttonsgetjson"})
public class GuestButtonsGetJSON extends HttpServlet {
	private static final long serialVersionUID = -2187991711238370988L;
	private static final Logger logger = LoggerFactory
			.getLogger(GuestButtonsGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(final HttpServletRequest request,
						   final HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		final String groupName = request.getParameter("group");

		if (groupName == null || groupName.trim().equals(""))
			throw new ServletException("You must pass a group parameter");

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();

		final PrintWriter out = response.getWriter();

		final ButtonEventsJSON buttonsJSON = new ButtonEventsJSON(true);

		final List<String> eventNames = buttonsJSON.getEventNames();
		for (final HttpTrigger httpTrigger : sbtsConfig.getHttpConfig().getGroupMap()
				.get(groupName)) {
			if (httpTrigger.isGuest())
				continue;

			eventNames.add(httpTrigger.getEventName());
		}

		out.print(gson.toJson(buttonsJSON));
	}

}
