package com.stalkedbythestate.sbts.streamer;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/mvc"})
public class Mvc extends HttpServlet {
	private static final long serialVersionUID = 8379886242412427110L;
	private static final Logger logger = Logger.getLogger(Mvc.class);

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("In mvc");
	    RequestDispatcher view = request.getRequestDispatcher("mvc.jsp");
	    
	    String name = "Kim";
	    request.setAttribute("name", name);
	    view.forward(request, response);
	}
}
