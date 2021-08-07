package com.stalkedbythestate.sbts.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewJSON {
	private static final Logger logger = Logger.getLogger(ViewJSON.class);
	private FreakApi freak;
	@Expose
	private int arraySize;
	@Expose
	private int actualSize;
	@Expose
	private SortedSet<Integer> cameraSet = new TreeSet<Integer>();
	@Expose
	private int[] offsetMapping;
	@Expose
	private String[] webPrefixes;
	@Expose
	private String[] cameraNames;
	@Expose
	private Map<Long, boolean[]> eventMap = new TreeMap<Long, boolean[]>();
	@Expose
	private Map<Long, String> eventDescMap = new HashMap<Long, String>();

	// These should not be exposed
	Map<Integer, CameraDevice> cameraDevices;
	SbtsDeviceConfig sbtsConfig;
	Map<Integer, Integer> camToOffsetMap = new HashMap<Integer, Integer>();
	String filterPattern;

	public ViewJSON() {
	}

	public ViewJSON(FreakApi freak, int arraySize,
                    SortedSet<Integer> cameraSet, List<String> eventFilterList) {
		this.freak = freak;
		if (eventFilterList != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("^(?:");
			int pieceCount = 0;
			for (String s : eventFilterList) {
				if (pieceCount > 0)
					sb.append("|");
				sb.append(s.replaceAll(
						"([\\(\\[\\{\\\\\\^\\-\\$\\|\\]\\}\\)\\?\\*\\+\\.])",
						"\\\\$1").replaceAll("\\\\\\*", ".*"));
				pieceCount++;
			}
			sb.append(")$");
			filterPattern = sb.toString();
			if (logger.isDebugEnabled())
				logger.debug("filterPattern is: " + filterPattern);
		}

		if (logger.isDebugEnabled())
			logger.debug("Constructing ViewJSON, arraySize: " + arraySize
					+ " cameraSet: " + cameraSet.toString());

		sbtsConfig = freak.getSbtsConfig();

		cameraDevices = sbtsConfig.getCameraConfig().getCameraDevices();
		if (logger.isDebugEnabled())
			logger.debug("cameraDevices: " + cameraDevices.toString());

		this.arraySize = arraySize;
		if (cameraSet.size() > arraySize)
			this.arraySize = cameraSet.size();

		offsetMapping = new int[arraySize];
		cameraNames = new String[arraySize];

		int offset = 0;
		for (int index : cameraSet) {
			if (!cameraDevices.containsKey(index)) {
				continue;
			}

			// Valid camera, add to the set
			this.cameraSet.add(index);
			cameraNames[offset] = cameraDevices.get(index).getName();
			camToOffsetMap.put(index, offset);

			offsetMapping[offset++] = index;
		}
		actualSize = offset;

		webPrefixes = new String[arraySize];

		for (int i = 0; i < actualSize; i++)
			webPrefixes[i] = sbtsConfig.getSettingsConfig().getWebPrefix();
	}

	public void filter(String startDateStr, String endDateStr, String times,
                       boolean remote, String webPrefix, VideoType videotype) {
		SbtsDeviceConfig sbtsConfig;

		if (logger.isDebugEnabled())
			logger.debug("Filtering, this is: " + this);

		String startTimeStr = null;
		String endTimeStr = null;
		if (times != null) {
			Pattern p1 = Pattern.compile("^(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)$");
			Matcher m1 = p1.matcher(times);
			if (m1.find()) {
				startTimeStr = m1.group(1) + ":00";
				endTimeStr = m1.group(2) + ":00";
			} else {
				Pattern p2 = Pattern
						.compile("^(\\d\\d:\\d\\d:\\d\\d)-(\\d\\d:\\d\\d:\\d\\d)$");
				Matcher m2 = p2.matcher(times);
				if (m2.find()) {
					startTimeStr = m2.group(1);
					endTimeStr = m2.group(2);
				}
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("In filter, remote: " + remote + " , startDateStr: "
					+ startDateStr + " , " + endDateStr);
		if (logger.isDebugEnabled())
			logger.debug("CameraSet.size(): " + cameraSet.size());

		if (cameraSet.size() <= 0)
			return;

		sbtsConfig = freak.getSbtsConfig();

		Map<Integer, CameraDevice> cameraDevices = sbtsConfig.getCameraConfig()
				.getCameraDevices();

		for (int camIndex : cameraSet) {
			CameraDevice cameraDevice = cameraDevices.get(camIndex);
			if (cameraDevice == null || !cameraDevice.isEnabled())
				continue;

			String dirname = null;
			File eventDir = null;
			try {
				dirname = freak.getSbtsBase() + "/disk/sbts/events/" + camIndex;
				eventDir = new File(dirname);
				if (!eventDir.canRead())
					continue;
			} catch (NullPointerException ne) {
				logger.error("Caught np during file");
				continue;
			} catch (Exception e1) {
				logger.error("Caught exception checking events");
				continue;
			}

			for (File dateDir : eventDir.listFiles()) {
				if (logger.isDebugEnabled())
					logger.debug("dateDir: " + dateDir.getName());
				if (!dateDir.isDirectory() || !dateDir.canRead()) {
					if (logger.isDebugEnabled())
						logger.debug("It's not a directory or cannot read, skipping: "
								+ dateDir.getPath());
					continue;
				}

				if (startDateStr != null
						&& startDateStr.compareTo(dateDir.getName()) > 0) {
					if (logger.isDebugEnabled())
						logger.debug("startDateStr is not null and it's in not in range, skipping: it's \""
								+ startDateStr + "\"");
					continue;
				}

				if (endDateStr != null
						&& endDateStr.compareTo(dateDir.getName()) <= 0) {
					if (logger.isDebugEnabled())
						logger.debug("endDateStr is not null and it's not in range, it's \""
								+ endDateStr + "\"");
					continue;
				}

				// Date is good, get files
				for (File f : dateDir.listFiles()) {
					if (videotype == VideoType.WEBM) {
						if (logger.isDebugEnabled())
							logger.debug("Checking webm");
						File webmHTML = new File(f.getPath().replaceAll(
								"/events/", "/webm/")
								+ ".htm");
						if (!webmHTML.isFile() || !webmHTML.canRead()) {
							if (logger.isDebugEnabled())
								logger.debug("It's not a directory or I can't read it, skipping");
							continue;
						}
					} else if (videotype == VideoType.MJPEG) {
						if (logger.isDebugEnabled())
							logger.debug("Checking mjpg");
						File mjpegDIR = new File(f.getPath().replaceAll(
								"/events/", "/dvr_images/"));
						if (!mjpegDIR.isDirectory() || !mjpegDIR.canRead()) {
							if (logger.isDebugEnabled())
								logger.debug("It's not a directory or I can't read it, skipping");
							continue;
						}
					}

					String eventName = f.getName();
					if (!eventName.matches("^\\d+$")) {
						if (logger.isDebugEnabled())
							logger.debug("Doesn't match the eventName pattern, skipping");
						continue;
					}
					long eventValue;
					try {
						eventValue = Long.parseLong(eventName);
						if (logger.isDebugEnabled())
							logger.debug("Parsed an eventValue");
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
						eventValue = 0L;
					}

					if (startTimeStr != null) {
						Date d = new Date(eventValue);
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						String timeString = sdf.format(d).toString();
						if (timeString.compareTo(startTimeStr) < 0
								|| timeString.compareTo(endTimeStr) > 0)
							continue;
					}

					// Read description into separate hash
					if (f.exists()) {
						if (logger.isDebugEnabled())
							logger.debug("The file exists");
						try {
							BufferedReader inStream = new BufferedReader(
									new InputStreamReader(
											new FileInputStream(f)));

							String firstLine;
							if (logger.isDebugEnabled())
								logger.debug("eventFilterMap: ");
							if ((firstLine = inStream.readLine()) != null) {
								if (filterPattern != null
										&& !firstLine.matches(filterPattern)) {
									inStream.close();
									continue;
								}
								eventDescMap.put(eventValue, firstLine);
							} else
								eventDescMap.put(eventValue, "");
							inStream.close();
						} catch (Exception e) {
							logger.error("Caught exception reading event file for list");
							e.printStackTrace();
						}
					} else {
						if (logger.isDebugEnabled())
							logger.debug("The file doesn't exist");
						eventDescMap.put(eventValue, "");
					}
					if (logger.isDebugEnabled())
						logger.debug("Filtered on the eventName ("
								+ eventDescMap.get(eventValue)
								+ ") now for the event itself");

					if (!eventMap.containsKey(eventValue)) {
						if (logger.isDebugEnabled())
							logger.debug("eventMap doesn't exists, creating");
						eventMap.put(eventValue, new boolean[arraySize]);
					}
					eventMap.get(eventValue)[camToOffsetMap.get(camIndex)] = true;
				}
			}
		}

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("gson: " + gson.toJson(this, ViewJSON.class));
	}

	public int getArraySize() {
		return arraySize;
	}

	public void setArraySize(int arraySize) {
		this.arraySize = arraySize;
	}

	public String[] getCameraNames() {
		return cameraNames;
	}

	public void setCameraNames(String[] cameraNames) {
		this.cameraNames = cameraNames;
	}

	public Map<Long, boolean[]> getEventMap() {
		return eventMap;
	}

	public void setEventMap(Map<Long, boolean[]> eventMap) {
		this.eventMap = eventMap;
	}

	public Map<Long, String> getEventDescMap() {
		return eventDescMap;
	}

	public void setEventDescMap(Map<Long, String> eventDescMap) {
		this.eventDescMap = eventDescMap;
	}

	public int[] getOffsetMapping() {
		return offsetMapping;
	}

	public void setOffsetMapping(int[] offsetMapping) {
		this.offsetMapping = offsetMapping;
	}

	public int getActualSize() {
		return actualSize;
	}

	public void setActualSize(int actualSize) {
		this.actualSize = actualSize;
	}

	public String[] getWebPrefixes() {
		return webPrefixes;
	}

	@Override
	public String toString() {
		return "ViewJSON [actualSize=" + actualSize + ", arraySize="
				+ arraySize + ", camToOffsetMap=" + camToOffsetMap
				+ ", cameraDevices=" + cameraDevices + ", cameraNames="
				+ Arrays.toString(cameraNames) + ", cameraSet=" + cameraSet
				+ ", eventDescMap=" + eventDescMap + ", eventMap=" + eventMap
				+ ", filterPattern=" + filterPattern + ", sbtsConfig="
				+ sbtsConfig + ", offsetMapping="
				+ Arrays.toString(offsetMapping) + ", webPrefixes="
				+ Arrays.toString(webPrefixes) + "]";
	}

}
