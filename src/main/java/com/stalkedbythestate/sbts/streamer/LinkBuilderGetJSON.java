package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.Link;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.CameraListJSON;
import com.stalkedbythestate.sbts.json.LinkBuilderJSON;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet(urlPatterns={"/linkbuildergetjson"})
public class LinkBuilderGetJSON extends HttpServlet {
	private static final long serialVersionUID = -8634871408748604652L;
	private static final Logger logger = Logger
			.getLogger(LinkBuilderGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	private List<RemoteFreakSpec> remoteFreakSpecList;
	private volatile RemoteFreakSpec remoteFreakSpec;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private void fetchRemoteCamListJSON(Collection<FreakDevice> freakDeviceList) {
		remoteFreakSpecList = new ArrayList<RemoteFreakSpec>();
		if (freakDeviceList.size() <= 0)
			return;

		if (logger.isDebugEnabled())
			logger.debug("About to create executor Service, size: "
					+ freakDeviceList.size());
		ExecutorService executorService = Executors.newFixedThreadPool(
				freakDeviceList.size(), new RemoteCamListThreadFactory());

		for (FreakDevice freakDevice : freakDeviceList) {
			remoteFreakSpec = new RemoteFreakSpec(freakDevice);
			remoteFreakSpecList.add(remoteFreakSpec);
			if (logger.isDebugEnabled())
				logger.debug("About to try spawning");
			try {
				if (logger.isDebugEnabled())
					logger.debug("Try spawning");
				executorService.execute(new HandleRemoteCamListFetch(
						remoteFreakSpec));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/linkbuildergetjson")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		LinkBuilderJSON linkBuilderJSON = new LinkBuilderJSON();
		linkBuilderJSON.setCameraList(sbtsConfig.getCameraConfig()
				.getCameraOrder());
		Map<String, SortedSet<Integer>> freakMap = linkBuilderJSON
				.getFreakMap();

		fetchRemoteCamListJSON(sbtsConfig.getFreakConfig().getFreakMap()
				.values());
		for (RemoteFreakSpec remoteFreakSpec : remoteFreakSpecList) {
			CameraListJSON cameraListJSON = null;
			try {
				cameraListJSON = remoteFreakSpec.getQueue().take();
				freakMap.put(remoteFreakSpec.getFreakDevice().getName(),
						cameraListJSON.getCameraList());
			} catch (InterruptedException e) {
				e.printStackTrace();
				linkBuilderJSON.setResult(false);
				linkBuilderJSON.getMessages().add(
						"Failed to fetch remote freak cameralist");
				out.print(gson.toJson(linkBuilderJSON));
				return;
			}

		}

		for (Link link : sbtsConfig.getLinksConfig().getLinkList()) {
			if (logger.isDebugEnabled())
				logger.debug("Got a link: " + link.getName() + " "
						+ link.getLink());
			linkBuilderJSON.getLinkList().add(link);
		}

		linkBuilderJSON.setResult(true);

		out.print(gson.toJson(linkBuilderJSON));
	}

}
