package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.LogJSON;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/systemloggetjson"})
public class SystemLogGetJSON extends HttpServlet {
	private static final long serialVersionUID = -8629726494228125487L;
	private static final Logger logger = Logger
			.getLogger(SystemLogGetJSON.class);
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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		LogJSON logJSON = new LogJSON(true);
		FileBlob fileBlob = new FileBlob();

		String log1;
		String log2;
		StringBuffer sb = new StringBuffer();

		try {
			log1 = fileBlob.getBlob("operations.log.1");
			sb.append(log1);
		} catch (Exception e) {
		}

		try {
			log2 = fileBlob.getBlob("operations.log");
			sb.append(log2);
		} catch (Exception e) {
		}

		logJSON.setLog(sb.toString());

		out.print(gson.toJson(logJSON));
	}

}
