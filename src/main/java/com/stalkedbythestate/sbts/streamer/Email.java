package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.EmailJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.sbtsdevice.config.EmailProvider;
import com.stalkedbythestate.sbts.sbtsdevice.config.EncryptionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.EmailProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/email"})
public class Email extends HttpServlet {
	private static final long serialVersionUID = 1968920481408107268L;
	private static final Logger logger = LoggerFactory.getLogger(Email.class);
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

		if (logger.isDebugEnabled())
			logger.debug("Check if GET or POST");

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting E-mail");
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

			Gson fromGson = new Gson();
			EmailJSON emailJSON = fromGson.fromJson(body, EmailJSON.class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("emailJSON has become: " + gson.toJson(emailJSON));

			EmailProvider emailProvider = new EmailProviderImpl();

			// The following three are fixed
			emailProvider.setName(emailJSON.getName());
			emailProvider.setDescription(emailJSON.getDescription());
			emailProvider.setMailhost(emailJSON.getMailhost());
			emailProvider.setFrom(emailJSON.getFrom());
			emailProvider.setUsername(emailJSON.getUsername());
			emailProvider.setPassword(emailJSON.getPassword());
			emailProvider.setPort(emailJSON.getPort());
			emailProvider.setEncryptionType(EncryptionType.set("ENC_" + emailJSON.getEncType()));

			// Replace existing E-mail config
			sbtsConfig.getEmailConfig().setEmailProvider(emailProvider);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();
			out.print(gson.toJson(resultMessage));
			opLogger.info("Updates E-mail");
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Email.jsp");
			view.forward(request, response);
		}

	}

}
