package com.stalkedbythestate.sbts.streamer;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/gettingstarted"})
public class GettingStarted extends HttpServlet {
	private static final long serialVersionUID = -8693038085071155968L;
	private static final Logger logger = Logger.getLogger(GettingStarted.class);
	private static final Logger opLogger = Logger.getLogger("operations");
//	private SbtsDeviceConfig sbtsConfig;
//	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		RequestDispatcher view = request.getRequestDispatcher("jsp/GettingStarted.jsp");
	    view.forward(request, response);
	}

}
