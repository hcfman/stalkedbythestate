package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
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

@WebServlet(urlPatterns={"/format"})
public class Format extends HttpServlet {
	private static final long serialVersionUID = -578974372574103580L;
	private static final Logger logger = LoggerFactory.getLogger(Format.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private void startFormat() {
/*
		Executor executor = Executors.newSingleThreadExecutor();
		Thread formatThread = new Thread(new Formatter(freak));
		formatThread.setName("formatter");
		sbtsConfig.getDiskConfig().setThread(formatThread);
		sbtsConfig.getDiskConfig().setProgressQueue(
				new LinkedBlockingQueue<Progress>());
		executor.execute(formatThread);
*/
	}

	@RequestMapping("/format")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();

		ResultMessage resultMessage = new ResultMessage(true, "");
		if (!sbtsConfig.getSettingsConfig().isCheckMount()) {
			resultMessage = new ResultMessage(false,
					"This SBTS has no sddisk, no formatting required");
			return;
		} else {
			if (sbtsConfig.getDiskConfig().getDiskState() == DiskState.NO_DISK) {
				resultMessage = new ResultMessage(false,
						"Please insert a disk first");
			} else if (sbtsConfig.getDiskConfig().getDiskState() == DiskState.FORMATTING) {
				resultMessage = new ResultMessage(
						false,
						"You are already formatting. Please wait till it's finished. If this takes an \"extremely\" long time (More than an hour for example), then turn the power off and then on again");
			} else if (freak.getUpdating().get()) {
				resultMessage = new ResultMessage(false,
						"Can't format disk, an update is in progress");
			} else {
				startFormat();
			}
		}

		PrintWriter out = response.getWriter();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.print(gson.toJson(resultMessage));
	}

}
