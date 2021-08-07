package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.Profile;
import com.stalkedbythestate.sbts.sbtsdevice.config.ProfileConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.ProfileImpl;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ProfileJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/profiles"})
public class Profiles extends HttpServlet {
	private static final long serialVersionUID = 1232096668177008738L;
	private static final Logger logger = Logger.getLogger(Profiles.class);
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

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting profiles");
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
			ProfileJSON[] profilesJSON = fromGson.fromJson(body,
					ProfileJSON[].class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("profilesJSON has become: "
						+ gson.toJson(profilesJSON));
			;

			ProfileConfig newProfileConfig = new ProfileConfig();
			for (ProfileJSON profileJSON : profilesJSON) {
				Profile profile = new ProfileImpl(profileJSON.getName(),
						profileJSON.getTagname(), profileJSON.getDescription());
				profile.setValidTimes(profileJSON.getTimeSpecs());
				if (logger.isDebugEnabled())
					logger.debug("Create profile: " + profile);
				newProfileConfig.add(profile);
			}

			// Replace existing Profiles config
			sbtsConfig.setProfileConfig(newProfileConfig);

			// Now save to disk
			freak.saveConfig();

			ResultMessage resultMessage = new ResultMessage(true, "");

			PrintWriter out = response.getWriter();
			out.print(gson.toJson(resultMessage));
			opLogger.info("Updated Profiles");
		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("jsp/Profiles.jsp");
			view.forward(request, response);
		}
	}

}
