package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.HttpTriggerEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.HttpTrigger;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns={"/net", "/guest/net"})
public class Net extends HttpServlet {
	private static final long serialVersionUID = 8142007339282202725L;
	private static final Logger logger = LoggerFactory.getLogger(Net.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;
	private boolean guest;
	private String failReason;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private boolean isAllowed(String eventName) {
		for (List<HttpTrigger> httpTriggers : sbtsConfig.getHttpConfig()
				.getGroupMap().values()) {
			for (HttpTrigger httpTrigger : httpTriggers) {
				if (eventName.equals(httpTrigger.getEventName())) {
					if (guest) {
						if (!httpTrigger.isGuest()) {
							failReason = "Button doesn't allow guest access";
							return false;
						} else
							return true;
					} else {
						return true;
					}
				}
			}
		}

		failReason = "There is no button defined for this event";
		return false;
	}

	@RequestMapping(value={"/net", "/guest/net"})
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Only allow post requests. Rudimentary protection against CSRF attack
		if (!request.getMethod().equals("POST")) {
			RequestDispatcher view = request
					.getRequestDispatcher("/jsp/content/components/ErrorPage.jsp");
			view.forward(request, response);
			return;
		}

		if (freak == null)
			freak = Freak.getInstance();

		if (!freak.isReady())
			return;

		sbtsConfig = freak.getSbtsConfig();

		guest = request.isUserInRole("guest");

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		String eventName = request.getParameter("event");
		out.println("Ok");
		out.close();

		String clientEventTimeString = request.getParameter("eventTime");
		long clientEventTime = 0;
		try {
			if (clientEventTimeString != null)
				clientEventTime = Long.parseLong(clientEventTimeString);
		} catch (NumberFormatException e) {
			opLogger.error("Can't parse client eventTime string ("
					+ clientEventTimeString + ")");
		}

		if (eventName == null || eventName.trim().equals(""))
			return;

		if (isAllowed(eventName)) {
			if (logger.isDebugEnabled())
				logger.debug("Delivering HTTP trigger event");
			freak.sendEvent(new HttpTriggerEvent(eventName, System
					.currentTimeMillis(), clientEventTime, request
					.isUserInRole("guest") ? true : false));
		} else
			opLogger.info("Attempt to fire event \"" + eventName + "\" : "
					+ failReason);
	}

}
