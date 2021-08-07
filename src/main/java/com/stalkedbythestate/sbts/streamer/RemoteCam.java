package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.ValidityChecks;
import com.stalkedbythestate.sbts.eventlib.RemoteCamTriggerEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@WebServlet(urlPatterns={"/remotecam", "/guest/remotecam"})
public class RemoteCam extends HttpServlet {
	private static final long serialVersionUID = -2384531361289352106L;
	private static final Logger logger = Logger.getLogger(RemoteCam.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;
	private boolean guest;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	@RequestMapping(value={"/remotecam", "/guest/remotecam"})
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		guest = request.isUserInRole("guest");

		if (!freak.isReady()) {
			out.println("Freak is not ready yet");
			return;
		}

		String eventTimeString = request.getParameter("eventTime");
		if (eventTimeString == null || eventTimeString.trim().equals("")) {
			logger.error("No eventTime parameter submitted");
			out.println("No eventTime parameter submitted");
			return;
		}

		String eventName = request.getParameter("eventName");
		if (eventName == null || eventName.trim().equals("")) {
			logger.error("No eventName parameter submitted");
			out.println("No eventName parameter submitted");
			return;
		}

		String description = request.getParameter("description");
		if (description == null || description.trim().equals("")) {
			logger.error("No description parameter submitted");
			out.println("No description parameter submitted");
			return;
		}

		String cameraListString = request.getParameter("cameralist");
		if (cameraListString == null || cameraListString.trim().equals("")) {
			logger.error("No cameraList selected");
			out.println("No cameraList selected");
			return;
		}

		// Malformed time
		if (!eventTimeString.matches("^\\d+$")) {
			logger.error("Malformed eventTime String submitted: "
					+ eventTimeString);
			out.println("Malformed eventTime String submitted");
			return;
		}

		if (!ValidityChecks.isEventNameValid(eventName)) {
			logger.error("Malformed eventName submitted: " + eventName);
			out.println("Malformed eventName submitted");
			return;
		}

		long eventTime = Long.parseLong(eventTimeString);
		if (!ValidityChecks.isEventTimeValid(eventTime)) {
			logger.error("Invalid eventTime submitted");
			out.println("Invalid eventTime submitted (Too small): "
					+ eventTimeString);
			return;
		}

		if (!cameraListString.matches("^\\d+(?:,\\d+)*$")) {
			logger.error("Malformed cameraListString: " + cameraListString);
			out.println("Malformed cameraListString");
			return;
		}

		sbtsConfig = freak.getSbtsConfig();

		String[] cameraListStringArray = cameraListString.split(",");

		Map<Integer, CameraDevice> cameraDevices = sbtsConfig.getCameraConfig()
				.getCameraDevices();

		SortedSet<Integer> cameraSet = new TreeSet<Integer>();

		for (String s : cameraListStringArray) {
			int camIndex = Integer.parseInt(s);
			CameraDevice cam;
			if ((cam = cameraDevices.get(camIndex)) == null) {
				logger.error("Invalid camera specified: " + s);
				out.println("Invalid camera specified: " + s);
				opLogger.error("Attempt to trigger camera from remote with invalid camera index ("
						+ camIndex + ")");
				return;
			}

			if (guest && !cam.isGuest()) {
				opLogger.error("Attempt to trigger camera ("
						+ camIndex
						+ ") from remote as guest but camera is not enabled for guest access");
				out.println("Attempt to trigger camera ("
						+ camIndex
						+ ") from remote as guest but camera is not enabled for guest access");
				return;
			}

			cameraSet.add(camIndex);
		}

		if (logger.isDebugEnabled())
			logger.debug("eventTimeString: " + eventTimeString);
		if (logger.isDebugEnabled())
			logger.debug("eventTime: " + eventTime);
		freak.sendEvent(new RemoteCamTriggerEvent(eventName, description,
				eventTime, request.isUserInRole("guest") ? true : false,
				cameraSet));

		out.println("Ok");
	}
}
