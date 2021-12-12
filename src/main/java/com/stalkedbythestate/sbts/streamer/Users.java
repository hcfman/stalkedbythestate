package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.json.UserJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/users"})
public class Users extends HttpServlet {
	private static final long serialVersionUID = 2989093926341717499L;
	private static final Logger logger = LoggerFactory.getLogger(Users.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
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

		if (request.getMethod().equals("POST")) {

			if (logger.isDebugEnabled())
				logger.debug("Posting users's");
			response.setContentType("application/json");

			if (logger.isDebugEnabled())
				logger.debug("content-type: "
						+ request.getHeader("content-type"));

			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(
							inputStream));
					char[] charBuffer = new char[128];
					int bytesRead = -1;
					while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
						stringBuilder.append(charBuffer, 0, bytesRead);
					}
				} else {
					stringBuilder.append("");
				}
			} catch (IOException ex) {
				throw ex;
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException ex) {
						throw ex;
					}
				}
			}
			String body = stringBuilder.toString();

			if (logger.isDebugEnabled())
				logger.debug("Body: " + body);

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			if (freak.getUpdating().get()) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"Cannot update users, an update is in progress");
				opLogger.info("Cannot update users, an update is in progress");

				out.print(gson.toJson(resultMessage));
				return;
			}

			synchronized (freak) {
				try {
					freak.mountReadWrite();

					Gson fromGson = new Gson();
					UserJSON[] userJSON = fromGson.fromJson(body,
							UserJSON[].class);
					if (logger.isDebugEnabled())
						logger.debug("userJSON has become: "
								+ gson.toJson(userJSON));

					ParseUsers parseUsers = new ParseUsers();
					try {
						parseUsers.updateTomcatUsers(userJSON);
					} catch (Exception e) {
						opLogger.error("Can't update the users file: "
								+ e.getMessage());
						resultMessage = new ResultMessage(false,
								"Can't update the users file: "
										+ e.getMessage());
					}

					String catalinaHome;

					catalinaHome = System.getenv("CATALINA_HOME");

					File tomcatUsersFile = new File(catalinaHome
							+ "/conf/tomcat-users.xml");
					File newTomcatUsersFile = new File(catalinaHome
							+ "/conf/newtomcat-users.xml");

					out.print(gson.toJson(resultMessage));
					opLogger.info("Updated Users");
					if (newTomcatUsersFile.exists()) {
						tomcatUsersFile.delete();
						newTomcatUsersFile.renameTo(tomcatUsersFile);
						freak.mountReadonly();
						Restarter.restart(freak);
					}
				} finally {
					freak.mountReadonly();
				}
			}

		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Users.jsp");
			view.forward(request, response);
		}
	}

}
