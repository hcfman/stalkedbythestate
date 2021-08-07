package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.UserJSON;
import com.stalkedbythestate.sbts.json.UsersJSON;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/usersgetjson"})
public class UsersGetJSON extends HttpServlet {
	private static final long serialVersionUID = 7022573235491085049L;
	private static final Logger logger = Logger.getLogger(UsersGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	private UsersJSON usersJSON;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		ParseUsers parseUsers = new ParseUsers();
		try {
			usersJSON = parseUsers.parse();
		} catch (XPathExpressionException | SAXException | ParserConfigurationException e) {
			logger.error("Can't parse tomcat-users.xml: " + e.getMessage());
			e.printStackTrace();
		}

		for (UserJSON userJSON : usersJSON.getUsers())
			userJSON.setPassword(null);

		out.print(gson.toJson(usersJSON));
	}

}
