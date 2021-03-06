package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ButtonJSON;
import com.stalkedbythestate.sbts.json.ButtonsJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.sbtsdevice.config.HttpTrigger;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(urlPatterns={"/buttongroups"})
public class ButtonGroups extends HttpServlet {
	private static final long serialVersionUID = -2948165239448735081L;
	private static final Logger logger = LoggerFactory.getLogger(ButtonGroups.class);
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
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting buttongroups's");
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

			Gson fromGson = new Gson();
			if (logger.isDebugEnabled())
				logger.debug("About to convert");
			ButtonsJSON buttonsJSON = fromGson.fromJson("{\"buttonGroups\":"
					+ body + "}", ButtonsJSON.class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("ButtonsJSON has become: "
						+ gson.toJson(buttonsJSON));

			Map<String, List<HttpTrigger>> newGroupMap = new ConcurrentHashMap<String, List<HttpTrigger>>();
			for (String groupName : buttonsJSON.getButtonGroups().keySet()) {
				List<HttpTrigger> buttonList = new ArrayList<HttpTrigger>();
				newGroupMap.put(groupName, buttonList);
				for (ButtonJSON buttonJSON : buttonsJSON.getButtonGroups().get(
						groupName)) {
					buttonList.add(new HttpTrigger(buttonJSON.getEventName(),
							buttonJSON.getDescription(), buttonJSON.isGuest()));
				}
			}

			sbtsConfig.getHttpConfig().setGroupMap(newGroupMap);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated Button Groups");
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/ButtonGroups.jsp");
			view.forward(request, response);
		}
	}

}