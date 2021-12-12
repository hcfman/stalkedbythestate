package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.eventlib.ConfigureEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.PhidgetJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetConstants;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetDeviceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns={"/phidgets"})
public class Phidgets extends HttpServlet {
	private static final long serialVersionUID = 9148081148821038145L;
	private static final Logger logger = LoggerFactory.getLogger(Phidgets.class);
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

			PhidgetJSON[] phidgetsJSON = fromGson.fromJson(body,
					PhidgetJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("phidgetsJSON has become: "
						+ gson.toJson(phidgetsJSON));
			if (logger.isDebugEnabled())
				logger.debug("phidgetsJSON: " + phidgetsJSON);

			Map<String, PhidgetDevice> phidgetMap = new HashMap<String, PhidgetDevice>();
			for (PhidgetJSON phidgetJSON : phidgetsJSON) {

				PhidgetDevice phidgetDevice = new PhidgetDeviceImpl(
						phidgetJSON.getName(), phidgetJSON.getDescription(),
						Integer.parseInt(phidgetJSON.getSerialNumber()),
						Integer.parseInt(phidgetJSON.getPortSize()));

				boolean[] initialInputState = phidgetDevice
						.getInitialInputState();
				boolean[] initialOutputState = phidgetDevice
						.getInitialOutputState();
				String[] onTriggerEventNames = phidgetDevice
						.getOnTriggerEventNames();
				String[] offTriggerEventNames = phidgetDevice
						.getOffTriggerEventNames();

				if (initialInputState != null)
					for (int i = 0; i < phidgetJSON.getInitialInputState().length; i++)
						if (i < PhidgetConstants.PHIDGET_PORT_SIZE)
							initialInputState[i] = phidgetJSON
									.getInitialInputState()[i];

				if (initialOutputState != null)
					for (int i = 0; i < phidgetJSON.getInitialOutputState().length; i++)
						if (i < PhidgetConstants.PHIDGET_PORT_SIZE)
							initialOutputState[i] = phidgetJSON
									.getInitialOutputState()[i];

				for (int i = 0; i < phidgetJSON.getOnTriggerEventNames().length; i++)
					if (i < PhidgetConstants.PHIDGET_PORT_SIZE)
						onTriggerEventNames[i] = phidgetJSON
								.getOnTriggerEventNames()[i];

				for (int i = 0; i < phidgetJSON.getOffTriggerEventNames().length; i++)
					if (i < PhidgetConstants.PHIDGET_PORT_SIZE)
						offTriggerEventNames[i] = phidgetJSON
								.getOffTriggerEventNames()[i];
				phidgetMap.put(phidgetJSON.getName(), phidgetDevice);
			}

			sbtsConfig.getPhidgetConfig().setPhidgetMap(phidgetMap);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			out.print(gson.toJson(resultMessage));

			if (logger.isDebugEnabled())
				logger.debug("Now restart phidgets");
			freak.sendPhidgetEvent(new ConfigureEvent());
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Phidgets.jsp");
			view.forward(request, response);
		}
	}

}