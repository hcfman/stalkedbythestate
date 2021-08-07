package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.Profile;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ProfileJSON;
import com.stalkedbythestate.sbts.json.ProfilesJSON;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/profilesgetjson"})
public class ProfilesGetJSON extends HttpServlet {
	private static final long serialVersionUID = 2358556701528161520L;
	private static final Logger logger = Logger
			.getLogger(ProfilesGetJSON.class);
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

		ProfilesJSON profilesJSON = new ProfilesJSON(true);

		for (Profile profile : sbtsConfig.getProfileConfig().getProfileList()) {

			ProfileJSON profileJSON = new ProfileJSON();
			profileJSON.setName(profile.getName());
			profileJSON.setDescription(profile.getDescription());
			profileJSON.setTagname(profile.getTagName());
			profileJSON.setTimeSpecs(profile.getValidTimes());

			profilesJSON.getProfilesJSON().add(profileJSON);
		}

		out.print(gson.toJson(profilesJSON));
	}

}
