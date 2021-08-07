package com.stalkedbythestate.sbts.streamer;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/links"})
public class Links extends HttpServlet {
	private static final long serialVersionUID = -5445870601382884148L;
	private static final Logger logger = Logger.getLogger(Links.class);

	
	public void init() throws ServletException {

	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

	    RequestDispatcher view = request.getRequestDispatcher("jsp/Links.jsp");
	    view.forward(request, response);
	}

}
