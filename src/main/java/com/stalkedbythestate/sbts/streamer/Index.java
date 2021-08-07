package com.stalkedbythestate.sbts.streamer;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/index"})
public class Index extends HttpServlet {
	private static final long serialVersionUID = -511390954168971595L;
	private static final Logger logger = Logger.getLogger(Index.class);

	
	public void init() throws ServletException {
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		if (logger.isDebugEnabled()) logger.debug("In index");

	    RequestDispatcher view = request.getRequestDispatcher("jsp/Index.jsp");
	    view.forward(request, response);
	}

}
