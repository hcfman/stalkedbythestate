package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.Progress;
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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns={"/dprogress"})
public class Dprogress extends HttpServlet {
	private static final long serialVersionUID = -8849210381743264903L;
	private static final Logger logger = LoggerFactory.getLogger(Dprogress.class);
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

		PrintWriter out = response.getWriter();

		Progress progress;

		LinkedBlockingQueue<Progress> progressQueue = sbtsConfig
				.getDownloadConfig().getProgressQueue();

		if (progressQueue == null) {
			progress = new Progress(0, "Download not started", false);
		} else {
			try {
				progress = progressQueue.poll(30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				progress = new Progress(0, "Error retrieving progress", false);
			}
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.print(gson.toJson(progress));
	}

}
