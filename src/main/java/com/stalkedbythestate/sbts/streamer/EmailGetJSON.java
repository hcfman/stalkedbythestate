package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.EmailJSON;
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

@WebServlet(urlPatterns={"/emailgetjson"})
public class EmailGetJSON extends HttpServlet {
	private static final long serialVersionUID = 8540530776357097308L;
	private static final Logger logger = LoggerFactory.getLogger(EmailGetJSON.class);
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

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		EmailJSON emailJSON = new EmailJSON();
		emailJSON.setResult(true);
		if (sbtsConfig.getEmailConfig() != null
				&& sbtsConfig.getEmailConfig().getEmailProvider() != null) {
			emailJSON.setName(sbtsConfig.getEmailConfig().getEmailProvider()
					.getName());
			emailJSON.setDescription(sbtsConfig.getEmailConfig()
					.getEmailProvider().getDescription());
			emailJSON.setMailhost(sbtsConfig.getEmailConfig().getEmailProvider()
					.getMailhost());
			emailJSON.setFrom(sbtsConfig.getEmailConfig().getEmailProvider()
					.getFrom());
			emailJSON.setUsername(sbtsConfig.getEmailConfig().getEmailProvider()
					.getUsername());
			emailJSON.setPassword(sbtsConfig.getEmailConfig().getEmailProvider()
					.getPassword());
			emailJSON.setPort(sbtsConfig.getEmailConfig().getEmailProvider()
					.getPort());
			emailJSON.setEncType(sbtsConfig.getEmailConfig().getEmailProvider()
					.getEncryptionType().getEncryptionType());
		}

		out.print(gson.toJson(emailJSON));
	}

}
