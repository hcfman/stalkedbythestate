package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.eventlib.ConfigureVideoEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.CameraJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
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
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

@WebServlet(urlPatterns={"/cameras"})
public class Cameras extends HttpServlet {
	private static final long serialVersionUID = -511390954168971595L;
	private static final Logger logger = LoggerFactory.getLogger(Cameras.class);
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
		if (logger.isDebugEnabled())
			logger.debug("In index");

		sbtsConfig = freak.getSbtsConfig();
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting cameras");
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
			CameraJSON[] camerasJSON = fromGson.fromJson(body,
					CameraJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("actionsJSON has become: "
						+ gson.toJson(camerasJSON));
			Map<Integer, CameraDevice> cameraDeviceMap = new TreeMap<Integer, CameraDevice>();
			int index = 1;
			for (CameraJSON cameraJSON : camerasJSON) {
				CameraDevice cameraDevice = cameraJSON.toCameraDevice();
				cameraDevice.setIndex(index);
				if (logger.isDebugEnabled())
					logger.debug("Adding camera device with index("
							+ cameraDevice.getIndex() + ") to the map");
				cameraDeviceMap.put(cameraDevice.getIndex(), cameraDevice);
				index++;
			}

			// Replace existing actions
			sbtsConfig.getCameraConfig().setCameraDevices(cameraDeviceMap);

			freak.sendEvent(new ConfigureVideoEvent());

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();
			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated Cameras");
		} else {
			Map<Integer, CameraDevice> cameraDevices = sbtsConfig
					.getCameraConfig().getCameraDevices();
			SortedSet<Integer> cameraOrder = sbtsConfig.getCameraConfig()
					.getCameraOrder();
			request.setAttribute("cameras", cameraDevices);
			request.setAttribute("cameraOrder", cameraOrder);
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Cameras.jsp");
			view.forward(request, response);
		}
	}

}
