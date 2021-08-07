package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.FreakDeviceImpl;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.ProtocolType;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.FreakJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
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

@WebServlet(urlPatterns={"/freaks"})
public class Freaks extends HttpServlet {
	private static final long serialVersionUID = 6852554347950656349L;
	private static final Logger logger = Logger.getLogger(Freaks.class);
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
				logger.debug("Posting freaks");
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

			Gson sourceJson = new Gson();
			if (logger.isDebugEnabled())
				logger.debug("About to convert");
			FreakJSON[] freaksJSON = sourceJson.fromJson(body,
					FreakJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("FreakJSON has become: " + gson.toJson(freaksJSON));

			Map<String, FreakDevice> freakMap = new HashMap<String, FreakDevice>();

			for (FreakJSON freakJSON : freaksJSON) {
				FreakDevice freakDevice = new FreakDeviceImpl(
						freakJSON.getName(), freakJSON.getDescription(),
						freakJSON.getHostname(), freakJSON.getPort(),
						freakJSON.getUsername(), freakJSON.getPassword(),
						freakJSON.getProtocol(), freakJSON.isVerifyHostname(),
						freakJSON.isGuest());
				if (freakDevice.getProtocol() == null)
					freakDevice.setProtocol(ProtocolType.HTTP);
				freakMap.put(freakDevice.getName(), freakDevice);
			}

			sbtsConfig.getFreakConfig().setFreakMap(freakMap);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated Freaks");
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Freaks.jsp");
			view.forward(request, response);
		}
	}

}