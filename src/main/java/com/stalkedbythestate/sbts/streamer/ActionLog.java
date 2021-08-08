package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.log4j.Logger;


@WebServlet(urlPatterns={"/actionlog"})
public class ActionLog extends HttpServlet {
	private static final long serialVersionUID = 8391229639062607622L;
	private static final Logger logger = Logger.getLogger(ActionLog.class);
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

		sbtsConfig = freak.getSbtsConfig();

		RequestDispatcher view = request
				.getRequestDispatcher("jsp/ActionLog.jsp");
		view.forward(request, response);
	}

}