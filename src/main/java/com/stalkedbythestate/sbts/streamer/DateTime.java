package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.DateTimeJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns={"/datetime"})
public class DateTime extends HttpServlet {
	private static final long serialVersionUID = 6139126215215384342L;
	private static final Logger logger = Logger.getLogger(DateTime.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private void changeDateTime(String year, String month, String day,
                                DateTimeJSON dateTimeJSON) {
		if (freak.getUpdating().get()) {
			opLogger.error("Can't set date/time, an update is in progress");
			return;
		}
	}

	@RequestMapping("/datetime")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();

		if (false && request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting datetime");
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

			Gson fromGson = new Gson();
			DateTimeJSON dateTimeJSON = fromGson.fromJson(body,
					DateTimeJSON.class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("dateTimeJSON has become: "
						+ gson.toJson(dateTimeJSON));

			// Check date
			String dateString = dateTimeJSON.getDate();
			if (dateString == null) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("Invalid date passed");

				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			Pattern datePattern = Pattern
					.compile("^(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)$");
			Matcher dateMatcher = datePattern.matcher(dateTimeJSON.getDate());
			if (!dateMatcher.find()) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("Invalid date passed");

				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}
			String year = dateMatcher.group(1);
			String month = dateMatcher.group(2);
			String day = dateMatcher.group(3);

			// Check times (Throws exception if broken)
			String hour = Integer.toString(dateTimeJSON.getTimeHour());
			String minute = Integer.toString(dateTimeJSON.getTimeMinute());

			// Now check pattern just in case
			if (!hour.matches("^\\d+$") || !minute.matches("^\\d+$")) {
				resultMessage.setResult(false);
				resultMessage.getMessages()
						.add("Invalid hour or minute passed");

				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			String timeZone = dateTimeJSON.getTimeZone();
			if (timeZone == null || timeZone.trim().equals("")
					|| timeZone.matches("\\.\\.")) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("Invalid time zone");

				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			File timeZoneFile = new File("/usr/share/zoneinfo/" + timeZone);
			if (!timeZoneFile.isFile() || !timeZoneFile.canRead()) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("Invalid time zone");

				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			// Check ntpserver
			String ntpServer;
			if ((ntpServer = dateTimeJSON.getNtpServer()) == null
					|| (ntpServer = ntpServer.trim()).equals("")) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("ntpServer must not be blank");
			} else if (!ntpServer
					.matches("^(?:(?:[a-zA-Z\\-0-9]*[a-zA-Z0-9])\\.?)+$")) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"Invalid characters found in the ntpServer");
			}

			if (freak.getUpdating().get()) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"Can't change the time, update is in progress");
				opLogger.info("Can't change the time, update is in progress");
			}

			if (!resultMessage.isResult()) {
				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			out.print(gson.toJson(resultMessage));
			out.close();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			opLogger.info("Setting date/time");
			changeDateTime(year, month, day, dateTimeJSON);
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("/jsp/content/components/ErrorPage.jsp");
			view.forward(request, response);
		}
	}

}
