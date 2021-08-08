package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.ViewJSON;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListJSON implements Serializable {
	private static final long serialVersionUID = 318925483924212254L;
	private static final Logger logger = Logger.getLogger(ListJSON.class);
	private Map<Integer, CameraDevice> cameraDevices;
	private SortedSet<Integer> cameraSet;
	private List<String> eventFilterList;

	private List<String> initialiseEventMap(HttpServletRequest request) {
		List<String> eventList;
		String eventListString = request.getParameter("eventlist");
		if (logger.isDebugEnabled())
			logger.debug("eventListString: " + eventListString);

		if (eventListString == null || eventListString.equals("")) {
			if (logger.isDebugEnabled())
				logger.debug("eventListStringis null or empty, returning");
			return null;
		}

		if (!eventListString.matches("^[^,]+(?:,[^,]+)*$"))
			return null;

		eventList = new ArrayList<>();

		if (logger.isDebugEnabled())
			logger.debug("eventListString: " + eventListString);
		for (String s : eventListString.split(",")) {
			if (logger.isDebugEnabled())
				logger.debug("event to add to map is: " + s);
			eventList.add(s);
		}

		return eventList;
	}

	private SortedSet<Integer> initialiseCameraSet(SbtsDeviceConfig sbtsConfig,
												   HttpServletRequest request) {
		SortedSet<Integer> cameraSet;
		boolean guest = request.isUserInRole("guest");

		String cameraListString = request.getParameter("cameralist");
		if (logger.isDebugEnabled())
			logger.debug("cameraListString: " + cameraListString);
		if (cameraListString != null)
			cameraListString = cameraListString.trim().replaceAll("\\s+", "");
		if (logger.isDebugEnabled())
			logger.debug("cameraListString: " + cameraListString);

		if (cameraListString != null
				&& cameraListString.matches("^\\d+(?:,\\d+)*")) {
			SortedSet<Integer> cameraOrder = sbtsConfig.getCameraConfig()
					.getCameraOrder();
			cameraSet = new TreeSet<>();
			if (logger.isDebugEnabled())
				logger.debug("Parsing cameraList string");
			for (String s : cameraListString.split(",")) {
				try {
					int cam = Integer.parseInt(s);
					if (cameraOrder.contains(cam)) {
						if (guest) {
							if (sbtsConfig.getCameraConfig().getCameraDevices()
									.get(cam).isGuest())
								cameraSet.add(cam);
						} else
							cameraSet.add(cam);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Not parsing cameraListString");
			cameraSet = sbtsConfig.getCameraConfig().getCameraOrder();
		}

		return cameraSet;
	}

	ViewJSON getJSON(boolean fromRemote, FreakApi freak, VideoType videoType,
					 HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SbtsDeviceConfig sbtsConfig;

		if (logger.isDebugEnabled())
			logger.debug("");
		if (!freak.isReady())
			return null;

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();

		cameraDevices = sbtsConfig.getCameraConfig().getCameraDevices();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// See if a range is selected
		long startTime = 0;
		Long now = System.currentTimeMillis();
		String startDateStr = request.getParameter("startdate");
		if (startDateStr != null && startDateStr.trim().equals(""))
			startDateStr = null;
		if (startDateStr != null
				&& !startDateStr
						.matches("^(?:\\d\\d\\d\\d-\\d\\d-\\d\\d|today)$"))
			throw new ServletException("Invalid start date format: "
					+ startDateStr);

		if (startDateStr != null && startDateStr.matches("^today$"))
			startDateStr = sdf.format(new Date());

		if (startDateStr == null) {
			// No start time specified, make it 24 hours ago
			startTime = now - (24 * 3600 * 1000);
			Date d = new Date(startTime);
			startDateStr = sdf.format(d);
			if (logger.isDebugEnabled())
				logger.debug("no start date specified, using \"" + startDateStr
						+ " (" + startTime + ")");
		}

		long endTime = 0;
		String endDateStr = request.getParameter("enddate");
		if (endDateStr != null && endDateStr.trim().equals(""))
			endDateStr = null;
		if (endDateStr != null
				&& !endDateStr.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d$"))
			throw new ServletException("Invalid end date format: ");

		if (endDateStr == null) {
			// No start time specified, make it 24 hours ago
			endTime = now + (24 * 3600 * 1000);
			Date d = new Date(endTime);
			endDateStr = sdf.format(d);
			if (logger.isDebugEnabled())
				logger.debug("no end date specified, using \"" + endDateStr
						+ " (" + endTime + ")");
		}

		String times = request.getParameter("timerange");
		if (times != null && !times.matches("^\\d\\d:\\d\\d-\\d\\d:\\d\\d")
				&& !times.matches("\\d\\d:\\d\\d:\\d\\d-\\d\\d:\\d\\d:\\d\\d"))
			throw new ServletException(
					"Invalid times format, must match nn:nn-nn:nn");

		// Initialise options
		cameraSet = initialiseCameraSet(sbtsConfig, request);
		eventFilterList = initialiseEventMap(request);
		FreakListFetcher freakFetcher = new FreakListFetcher(sbtsConfig);
		List<RemoteCameraSpec> remoteCameraSpecList = freakFetcher
				.initialiseFreakList(request.getParameter("freaklist"),
						videoType, startDateStr, endDateStr, times,
						eventFilterList);

		if (logger.isDebugEnabled())
			logger.debug("cameraSet: " + cameraSet.toString());

		int totalArraySize = cameraSet.size();
		for (RemoteCameraSpec remoteCameraSpec : remoteCameraSpecList) {
			totalArraySize += remoteCameraSpec.getCameraSet().size();
		}

		// Spawn threads to fetch other data
		if (remoteCameraSpecList.size() > 0) {
			if (logger.isDebugEnabled())
				logger.debug("Spawn handlers (remoteCameraSpecList): "
						+ remoteCameraSpecList);
			freakFetcher.fetchRemoteCamJSON();

		}

		if (logger.isDebugEnabled())
			logger.debug("Now get my normal JSON");

		if (logger.isDebugEnabled())
			logger.debug("About to filter eventFilterMap: " + eventFilterList);
		ViewJSON viewJSON = new ViewJSON(freak, totalArraySize, cameraSet,
				eventFilterList);
		viewJSON.filter(startDateStr, endDateStr, times, false, sbtsConfig
				.getSettingsConfig().getWebPrefix(), videoType);

		// Clear the prefix for the current host
		if (!fromRemote)
			for (int i = 0; i < viewJSON.getActualSize(); i++)
				viewJSON.getWebPrefixes()[i] = "";

		if (logger.isDebugEnabled())
			logger.debug("Done the normal filtering");

		// Merge in values
		int currentOffset = viewJSON.getActualSize();
		for (RemoteCameraSpec remoteCameraSpec : remoteCameraSpecList) {
			if (logger.isDebugEnabled())
				logger.debug("Read from the queue");
			ViewJSON remoteViewJSON;
			try {
				if (logger.isDebugEnabled())
					logger.debug("Try and get from the remote queue");
				remoteViewJSON = remoteCameraSpec.getQueue().take();

				// Merge header values
				for (int remoteOffset = 0; remoteOffset < remoteViewJSON
						.getActualSize(); remoteOffset++) {
					viewJSON.getOffsetMapping()[currentOffset + remoteOffset] = remoteViewJSON
							.getOffsetMapping()[remoteOffset];
					viewJSON.getWebPrefixes()[currentOffset + remoteOffset] = remoteViewJSON
							.getWebPrefixes()[remoteOffset];
					viewJSON.getCameraNames()[currentOffset + remoteOffset] = remoteViewJSON
							.getCameraNames()[remoteOffset];
				}

				// Merge event description map
				for (long eventValue : remoteViewJSON.getEventDescMap()
						.keySet())
					if (!viewJSON.getEventDescMap().containsKey(eventValue))
						viewJSON.getEventDescMap().put(
								eventValue,
								remoteViewJSON.getEventDescMap()
										.get(eventValue));

				// Merge the event map
				for (long eventValue : remoteViewJSON.getEventMap().keySet()) {
					// Allocate if not there already
					if (!viewJSON.getEventMap().containsKey(eventValue))
						viewJSON.getEventMap().put(eventValue,
								new boolean[viewJSON.getArraySize()]);

					// Now copy in the values from the remoteViewJSON
					boolean[] tempArray = viewJSON.getEventMap()
							.get(eventValue);
					for (int i = 0; i < remoteViewJSON.getActualSize(); i++)
						tempArray[currentOffset + i] = remoteViewJSON
								.getEventMap().get(eventValue)[i];
				}

				if (logger.isDebugEnabled())
					logger.debug("Got from the remote queue");
				currentOffset += remoteViewJSON.getActualSize();
				viewJSON.setActualSize(currentOffset);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("Final gson: " + gson.toJson(viewJSON, ViewJSON.class));

		return viewJSON;
	}

	public Map<Integer, CameraDevice> getCameraDevices() {
		return cameraDevices;
	}

	public SortedSet<Integer> getCameraSet() {
		return cameraSet;
	}

}
