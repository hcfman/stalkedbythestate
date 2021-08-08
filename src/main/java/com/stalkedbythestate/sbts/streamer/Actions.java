package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ActionJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns={"/actions"})
public class Actions extends HttpServlet {
	private static final Logger logger = Logger.getLogger(Actions.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private static final Logger actionLogger = Logger.getLogger("action");
	private static final long serialVersionUID = -1592607637479736028L;
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak = null;

	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    if (freak == null)
            freak = Freak.getInstance();

		if (!freak.isReady())
			return;

		sbtsConfig = freak.getSbtsConfig();
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting actions");
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

			// Method 2
			// DataInputStream in = new DataInputStream((InputStream)
			// request.getInputStream());
			//
			//
			// String text = in.readUTF();
			//
			// if (logger.isDebugEnabled()) logger.debug("Text is: " + text);

			Gson fromGson = new Gson();
			ActionJSON[] actionsJSON = fromGson.fromJson(body,
					ActionJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("actionsJSON has become: "
						+ gson.toJson(actionsJSON));
			List<Action> actionList = new ArrayList<Action>();
			for (ActionJSON actionJSON : actionsJSON) {
				Action action = actionJSON.toAction();

				actionList.add(action);
			}

			// Replace existing actions
			sbtsConfig.getActionConfig().setActionList(actionList);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			// if (logger.isDebugEnabled())
			// logger.debug("Received from the post: " +
			// gson.toJson(actionsJSON));

			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated Actions");
			actionLogger.info("Updated Actions");
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Serving actions to edit");
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Actions.jsp");
			view.forward(request, response);
		}

	}
}
