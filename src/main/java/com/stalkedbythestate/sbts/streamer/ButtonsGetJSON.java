package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.HttpTrigger;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ButtonJSON;
import com.stalkedbythestate.sbts.json.ButtonsJSON;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns={"/buttonsgetjson"})
public class ButtonsGetJSON extends HttpServlet {
	private static final long serialVersionUID = -5702234811980684241L;
	private static final Logger logger = Logger.getLogger(ButtonsGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	FreakApi freak;

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

		ButtonsJSON buttonsJSON = new ButtonsJSON(true);
		for (String groupName : sbtsConfig.getHttpConfig().getGroupsAsList()) {
			List<ButtonJSON> buttonList = new ArrayList<ButtonJSON>();
			for (HttpTrigger httpTrigger : sbtsConfig.getHttpConfig()
					.getGroupMap().get(groupName)) {
				ButtonJSON buttonJSON = new ButtonJSON();
				buttonJSON.setEventName(httpTrigger.getEventName());
				buttonJSON.setDescription((httpTrigger.getDescription()));
				buttonJSON.setGuest(httpTrigger.isGuest());
				buttonList.add(buttonJSON);
			}
			buttonsJSON.getButtonGroups().put(groupName, buttonList);
		}

		out.print(gson.toJson(buttonsJSON));
	}

}
