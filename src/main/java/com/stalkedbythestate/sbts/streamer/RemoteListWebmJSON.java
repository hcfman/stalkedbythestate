package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ViewJSON;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/guest/remotewebmjson", "/remotewebmjson"})

public class RemoteListWebmJSON extends HttpServlet {
	private static final long serialVersionUID = -3756468168136351794L;
	SbtsDeviceConfig sbtsConfig;
	FreakApi freak;
	ListService listService;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
//		listService = new ListService();
	}

	@RequestMapping(value={"/guest/remotewebmjson", "/remotewebmjson"})
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		if (listService == null)
			listService = new ListService();

		ListJSON listJSON = new ListJSON();
		ViewJSON viewJSON = listJSON.getJSON(true, freak, VideoType.WEBM,
				request, response);
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting().create();
		out.print(gson.toJson(viewJSON));
	}

}
