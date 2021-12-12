package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.UpdateCheckJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/checkupdates"})
public class CheckUpdates extends HttpServlet {
	private static final long serialVersionUID = 5121599439000072562L;
	private static final Logger logger = LoggerFactory.getLogger(CheckUpdates.class);
	private SbtsDeviceConfig sbtsConfig;
	FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	public UpdateCheckJSON getUpdateCheckResult() throws Exception {
		return null;
	}

	@RequestMapping("/checkupdates")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();

		UpdateCheckJSON updateCheckJSON = null;
		try {
			updateCheckJSON = getUpdateCheckResult();
		} catch (Exception e) {
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		out.print(gson.toJson(updateCheckJSON));
	}

}
