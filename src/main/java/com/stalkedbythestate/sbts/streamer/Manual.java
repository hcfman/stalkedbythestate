package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/manual"})
public class Manual extends HttpServlet {
	private static final long serialVersionUID = -1858469137764609827L;
	private static final Logger logger = LoggerFactory.getLogger(Manual.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
//	private SbtsDeviceConfig sbtsConfig;
//	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		RequestDispatcher view = request.getRequestDispatcher("jsp/Manual.jsp");
	    view.forward(request, response);
	}

}
