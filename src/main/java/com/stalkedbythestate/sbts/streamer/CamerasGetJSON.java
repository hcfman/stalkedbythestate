package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.CameraJSON;
import com.stalkedbythestate.sbts.json.CamerasJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

@WebServlet(urlPatterns={"/camerasgetjson"})
public class CamerasGetJSON extends HttpServlet {
	private static final long serialVersionUID = 8835361381758906332L;
	private static final Logger logger = LoggerFactory.getLogger(CamerasGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	FreakApi freak;

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

		CamerasJSON camerasJSON = new CamerasJSON(true);

		TreeMap<Integer, CameraDevice> tm = (TreeMap<Integer, CameraDevice>) sbtsConfig
				.getCameraConfig().getCameraDevices();
		for (Map.Entry<Integer, CameraDevice> entry : tm.entrySet()) {
			CameraDevice camera = entry.getValue();

			CameraJSON cameraJSON = new CameraJSON();
			cameraJSON.setName(camera.getName());
			cameraJSON.setDescription(camera.getDescription());
			cameraJSON.setIndex(camera.getIndex());
			cameraJSON.setEnabled(camera.isEnabled());
			cameraJSON.setUrl(camera.getUrl());
			cameraJSON.setUsername(camera.getUsername());
			cameraJSON.setPassword(camera.getPassword());
			cameraJSON.setContinueSeconds(camera.getContinueSeconds());
			cameraJSON.setBufferSeconds(camera.getBufferSeconds());
			cameraJSON.setFramesPerSecond(camera.getFramesPerSecond());
			cameraJSON.setPriority(camera.getPriority());
			cameraJSON.setCachingAllowed(camera.isCachingAllowed());
			cameraJSON.setGuest(camera.isGuest());

			camerasJSON.getCameras().add(cameraJSON);
		}

		out.print(gson.toJson(camerasJSON));
	}

}
