package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/system"})
public class SystemDetails extends HttpServlet {
	private static final long serialVersionUID = 6345590433774197386L;
	private static final Logger logger = Logger.getLogger(SystemDetails.class);

	
	public void init() throws ServletException {

	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

	    RequestDispatcher view = request.getRequestDispatcher("jsp/System.jsp");
	    view.forward(request, response);
	}

}
