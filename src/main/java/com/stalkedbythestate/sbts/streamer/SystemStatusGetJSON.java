package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.diskwatchdog.SpaceJSON;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.json.CameraStatusJSON;
import com.stalkedbythestate.sbts.json.SystemStatusJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
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

@WebServlet(urlPatterns={"/systemstatusgetjson"})
public class SystemStatusGetJSON extends HttpServlet {
	private static final Logger logger = LoggerFactory
			.getLogger(SystemStatusGetJSON.class);
	private static final long serialVersionUID = -4661056813441955903L;
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private SpaceJSON getSpace() {
		ScriptRunner scriptRunner = new ScriptRunner();
		;
		ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
				freak.getSbtsBase() + "/bin/getspace.sh", "getspace.sh");
		SpaceJSON spaceJSON = new SpaceJSON();
		if (scriptRunnerResult.getResult() == 0) {
			Gson fromGson = new Gson();
			spaceJSON = fromGson.fromJson(scriptRunnerResult.getOutput(),
					SpaceJSON.class);

			return spaceJSON;
		}

		return null;
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		SystemStatusJSON systemStatusJSON = new SystemStatusJSON(true);

		for (CameraDevice cameraDevice : sbtsConfig.getCameraConfig()
				.getCameraDevices().values()) {
			if (logger.isDebugEnabled())
				logger.debug("Looping for cam: " + cameraDevice.getIndex());
			CameraStatusJSON cameraStatusJSON = new CameraStatusJSON(
					cameraDevice.getName(), cameraDevice.getIndex(),
					cameraDevice.isUp());

			systemStatusJSON.getCameraStatus().add(cameraStatusJSON);
		}
		Freak freak = Freak.getInstance();

		systemStatusJSON.setRfxcomStatus(freak.getRfxcomHandler()
				.getRfxcomController().isConnected());

		for (PhidgetDevice phidgetDevice : sbtsConfig.getPhidgetConfig()
				.getPhidgetMap().values()) {
			systemStatusJSON.getPhidgetStatus().put(
					phidgetDevice.getSerialNumber(),
					phidgetDevice.isConnected());
		}

		if (sbtsConfig.getDiskConfig().getDiskState() != DiskState.ALL_GOOD) {
			systemStatusJSON.setDiskUp(false);
			systemStatusJSON.setDiskMessage(sbtsConfig.getDiskConfig()
					.getLastMessage());
		} else {
			systemStatusJSON.setDiskUp(true);
			SpaceJSON spaceJSON = getSpace();
			if (spaceJSON != null) {
				long used = spaceJSON.getUsed();
				long total = spaceJSON.getAvailable() + used;
				systemStatusJSON
						.setDiskMessage(spaceJSON != null ? ("Size: "
								+ (total / 1024) + "MB, Used: " + (spaceJSON
								.getUsed() / 1024))
								+ "MB ("
								+ (used * 100 / total) + "%)" : "");
			} else {
				systemStatusJSON
						.setDiskMessage("Failed to determine space used");
			}
		}

		out.print(gson.toJson(systemStatusJSON));
	}

}
