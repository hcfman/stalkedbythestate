package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.Watchdog;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.WatchdogImpl;
import com.stalkedbythestate.sbts.eventlib.ConfigureWatchdogEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.json.WatchdogJSON;
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

@WebServlet(urlPatterns={"/watchdogs"})
public class Watchdogs extends HttpServlet {
	private static final long serialVersionUID = 6073400998026456518L;
	private static final Logger logger = Logger.getLogger(Watchdogs.class);
	SbtsDeviceConfig sbtsConfig;
	FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting buttongroups's");
			response.setContentType("application/json");

			if (logger.isDebugEnabled())
				logger.debug("content-type: " + request.getHeader("content-type"));

			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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

			// WatchdogJSON
			if (logger.isDebugEnabled())
				logger.debug("About to convert");
			WatchdogJSON[] watchdogList = fromGson.fromJson(body, WatchdogJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("watchdogList has become: " + gson.toJson(watchdogList));

			Map<String, Watchdog> triggerMap = new HashMap<String, Watchdog>();
			for (WatchdogJSON watchdogJSON : watchdogList) {
				Watchdog watchdog = new WatchdogImpl(watchdogJSON.getResult(), watchdogJSON.getWithinSeconds(),
						watchdogJSON.getTriggerEventNames());
				triggerMap.put(watchdogJSON.getResult(), watchdog);
			}
			sbtsConfig.getWatchdogConfig().setTriggerMap(triggerMap);

			// Now save to disk
			freak.saveConfig();

			freak.sendEvent(new ConfigureWatchdogEvent());

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();

			out.print(gson.toJson(resultMessage));
		} else {
			RequestDispatcher view = request.getRequestDispatcher("jsp/Watchdogs.jsp");
			view.forward(request, response);
		}
	}

}