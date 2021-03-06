package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/view"})
public class View extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(View.class);
	private static final long serialVersionUID = 2487130599843615117L;
	SbtsDeviceConfig sbtsConfig;
	FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		if (!freak.isReady())
			return;
		String cam = request.getParameter("cam");
		String event = request.getParameter("t");
		
		response.setContentType("text/html");

		request.setAttribute("cam", cam);
		request.setAttribute("t", event);
		
		RequestDispatcher view = request.getRequestDispatcher("jsp/view.jsp");
		view.forward(request, response);
	}
}
