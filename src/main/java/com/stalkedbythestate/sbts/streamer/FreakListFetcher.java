package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreakListFetcher {
	private final Logger logger = Logger.getLogger(FreakListFetcher.class);
	private SbtsDeviceConfig sbtsConfig;
	private VideoType videoType;
	private List<RemoteCameraSpec> remoteCameraSpecList;
	
	public FreakListFetcher(SbtsDeviceConfig sbtsConfig) {
		this.sbtsConfig = sbtsConfig;
	}

	public List<RemoteCameraSpec> initialiseFreakList(String freakListString, VideoType videoType, String startDateStr, String endDateStr, String times, List<String> eventFilterList) {
		if (logger.isDebugEnabled()) logger.debug("Parsing freak list");
		remoteCameraSpecList = new ArrayList<RemoteCameraSpec>();
		this.videoType = videoType;
		
		if (logger.isDebugEnabled()) logger.debug("freakList: " + freakListString);
		if (freakListString != null)
			freakListString = freakListString.trim();
	    	
		if (logger.isDebugEnabled()) logger.debug("cameraListString: " + freakListString);
		
		if (freakListString != null && freakListString.matches("^(?:\\w+\\:\\d+(?:,\\d+)*)(?:\\s+\\w+\\:\\d+(?:,\\d+)*)*$")) {
			if (logger.isDebugEnabled()) logger.debug("It matches");
			if (logger.isDebugEnabled()) logger.debug("Parsing cameraList string");
			for (String remoteSpecString : freakListString.split("\\s+")) {
				if (logger.isDebugEnabled()) logger.debug("RemoteSpec: " + remoteSpecString);
				Pattern pattern = Pattern.compile("^(\\w+):(.*)$");
				Matcher matcher = pattern.matcher(remoteSpecString);
				RemoteCameraSpec remoteCameraSpec = null;
				if (matcher.matches()) {
					if (logger.isDebugEnabled()) logger.debug("Matches one freakSpec");
					String freakString = matcher.group(1);
					if (sbtsConfig.getFreakConfig().getFreakMap().containsKey(freakString)) {
						for (String camString : matcher.group(2).split(",")) {
							if (remoteCameraSpec == null) {
								if (logger.isDebugEnabled()) logger.debug("remoteCameraSpecList is null, creating");
								remoteCameraSpec = new RemoteCameraSpec();
								remoteCameraSpec.setStartDateStr(startDateStr);
								remoteCameraSpec.setEndDateStr(endDateStr);
								remoteCameraSpec.setTimes(times);
								remoteCameraSpec.setEventFilterList(eventFilterList);
								
								remoteCameraSpecList.add(remoteCameraSpec);
							}
							remoteCameraSpec.setFreakName(freakString);
							try {
								if (logger.isDebugEnabled()) logger.debug("camString: " + camString);
								int camValue = Integer.parseInt(camString);
								remoteCameraSpec.getCameraSet().add(camValue);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							
						}
					} else {
						if (logger.isDebugEnabled()) logger.debug("Not a valid freak");
					}
				}
			}
		} else {
			if (logger.isDebugEnabled()) logger.debug("It's null or doesn't match");
		}
		
		return remoteCameraSpecList;
	}
	
	public void fetchRemoteCamJSON() {
		if (logger.isDebugEnabled()) logger.debug("About to create executor Service, size: " + remoteCameraSpecList.size());
		ExecutorService executorService = Executors.newFixedThreadPool(remoteCameraSpecList.size(),
				new RemoteCamThreadFactory());
		for (RemoteCameraSpec remoteCameraSpec : remoteCameraSpecList) {
			if (logger.isDebugEnabled()) logger.debug("About to try spawning");
			try {
				if (logger.isDebugEnabled()) logger.debug("Try spawning");
				executorService.execute(new HandleRemoteCamFetch(sbtsConfig, videoType, remoteCameraSpec));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
