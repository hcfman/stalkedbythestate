package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.RfxcomConfig;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import com.stalkedbythestate.sbts.eventlib.ConfigureRfxcomEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.json.RfxcomCommandJSON;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns={"/rfxcom"})
public class Rfxcom extends HttpServlet {
	private static final long serialVersionUID = -570836224066068205L;
	private static final Logger logger = Logger.getLogger(Rfxcom.class);
	private static final Logger opLogger = Logger.getLogger("operations");
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
				logger.debug("Posting rfxcom's");
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
			RfxcomCommandJSON[] rfxcomCommandJSON = fromGson.fromJson(body,
					RfxcomCommandJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("RfxcomJSON has become: "
						+ gson.toJson(rfxcomCommandJSON));

			Map<String, RfxcomCommand> rfxcomCommandMap = new HashMap<String, RfxcomCommand>();
			for (RfxcomCommandJSON commandJSON : rfxcomCommandJSON) {
				rfxcomCommandMap.put(commandJSON.getName(),
						commandJSON.toRfxcomDevice());
			}
			RfxcomConfig rfxcomConfig = new RfxcomConfig();
			rfxcomConfig.setCommands(rfxcomCommandMap);

			sbtsConfig.setRfxcomConfig(rfxcomConfig);
			freak.sendEvent(new ConfigureRfxcomEvent());

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated rfxcom");
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/rfxcom.jsp");
			view.forward(request, response);
		}
	}

}
