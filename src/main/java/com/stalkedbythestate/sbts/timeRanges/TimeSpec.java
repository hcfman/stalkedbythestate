package com.stalkedbythestate.sbts.timeRanges;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.xpath.XPath;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TimeSpec {
	private static final String[] daysList = { "Sunday", "Sunday", "Monday",
			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private List<TimeRange> times;
	private Set<Integer> dows;

	public static String dayToString(int dayOfWeek) {
		if (dayOfWeek < 1 || dayOfWeek > 7)
			return null;
		return daysList[dayOfWeek];
	}

	public boolean within(long theTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(theTime));
		
		if (dows != null && !dows.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
//			System.out.println("within: dows is not null but doesn't match, DAY_OF_WEEK is(" + Calendar.DAY_OF_WEEK + ", TimeSpec dows(" + dows + ") times: (" + times.toString() + ")");
			return false;
		}
		
		if (times == null) {
//			System.out.println("within: There are no times specified, returning false");
			return false;
		}
		
		for (TimeRange timeRange : times) {
//			System.out.println("within: Checking within range:" + timeRange);
			if (timeRange.within(theTime))
				return true;
		}
		
//		System.out.println("within: don't match anything returning false");
		return false;
	}

	public List<TimeRange> getTimes() {
		return times;
	}

	public void setTimes(List<TimeRange> times) {
		this.times = times;
	}

	public Set<Integer> getDows() {
		return dows;
	}

	public void setDows(Set<Integer> dows) {
		this.dows = dows;
	}

	public static List<TimeSpec> parseTimeSpecs(XPath xpath, Element node)
			throws Exception {
//		System.out.println("\nIn parseTimeSpecs, node: " + node);
		
		List<TimeSpec> timeSpecs = new ArrayList<TimeSpec>();

		NodeList timeSpecListNodes = node.getElementsByTagName("timespecs");
		if (timeSpecListNodes.getLength() <= 0) {
			return timeSpecs;
		}

		Element timeSpecsElement = (Element) timeSpecListNodes.item(0);

		NodeList timeSpecList = timeSpecsElement.getElementsByTagName("timespec");

		for (int specIndex = 0; specIndex < timeSpecList.getLength(); specIndex++) {
			Element timeSpecElement = (Element) timeSpecList.item(specIndex);

			NodeList timeRangesList = timeSpecElement
					.getElementsByTagName("timeRanges");
			NodeList dowsList = timeSpecElement.getElementsByTagName("dows");

			TimeSpec timeSpec = new TimeSpec();
			timeSpecs.add(timeSpec);
			List<TimeRange> timeRanges = new ArrayList<TimeRange>();
			timeSpec.setTimes(timeRanges);
			Set<Integer> dows = null;
			dows = new TreeSet<Integer>();
			timeSpec.setDows(dows);
			
			if (timeRangesList.getLength() <= 0 && dowsList.getLength() <= 0)
				continue;

			for (int j = 0; j < timeRangesList.getLength(); j++) {
				Element timeRangesElement = (Element) timeRangesList.item(j);

				NodeList timeRangeList = timeRangesElement
						.getElementsByTagName("timeRange");

				for (int k = 0; k < timeRangeList.getLength(); k++) {
					Element TimeRangeElement = (Element) timeRangeList.item(k);

					NodeList startTimeList = TimeRangeElement
							.getElementsByTagName("startTime");
					String startTime = null;
					if (startTimeList.getLength() > 0)
						startTime = startTimeList.item(0).getTextContent();

					NodeList endTimeList = TimeRangeElement
							.getElementsByTagName("endTime");
					String endTime = null;
					if (endTimeList.getLength() > 0)
						endTime = endTimeList.item(0).getTextContent();

					timeRanges.add(new TimeRange(startTime, endTime));
				}
			}

			for (int l = 0; l < dowsList.getLength(); l++) {
				Element dowsElement = (Element) dowsList.item(l);
				String dowsString = dowsElement.getTextContent();

				String[] dowStrings = null;
				if (dowsString != null) {
					dowStrings = dowsString.split(",");
				}
				for (String dow : dowStrings)
					dows.add(Integer.parseInt(dow));
			}

			timeSpec.setDows(dows);
			timeSpec.setTimes(timeRanges);
		}

		return timeSpecs;
	}

	@Override
	public String toString() {
		return "TimeSpec [dows=" + dows + ", times=" + times + "]";
	}

}
