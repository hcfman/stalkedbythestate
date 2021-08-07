package com.stalkedbythestate.sbts.sbtsdevice.config;

import com.stalkedbythestate.sbts.timeRanges.TimeRange;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Set;

public class TimeSpecToXML {

	synchronized public static Element toXML(Document doc, TimeSpec spec,
                                             int offset) {
		String offsetString = StringHelper.repeatChar('\t', offset);

		Element timeSpec = doc.createElement("timespec");
		timeSpec.appendChild(doc.createTextNode("\n" + offsetString + "\t\t"));
		Element timeRangesElement = doc.createElement("timeRanges");
		timeSpec.appendChild(timeRangesElement);
		for (TimeRange range : spec.getTimes()) {
			timeRangesElement.appendChild(doc.createTextNode("\n"
					+ offsetString + "\t\t\t"));
			Element timeRangeElement = doc.createElement("timeRange");
			timeRangesElement.appendChild(timeRangeElement);

			timeRangeElement.appendChild(doc.createTextNode("\n" + offsetString
					+ "\t\t\t\t"));
			Element startTime = doc.createElement("startTime");
			timeRangeElement.appendChild(startTime);
			startTime.appendChild(doc.createTextNode(String.format(
					"%02d:%02d:%02d", range.getStartHour(),
					range.getStartMin(), range.getStartSec())));

			timeRangeElement.appendChild(doc.createTextNode("\n" + offsetString
					+ "\t\t\t\t"));
			Element endTime = doc.createElement("endTime");
			timeRangeElement.appendChild(endTime);
			endTime.appendChild(doc.createTextNode(String.format(
					"%02d:%02d:%02d", range.getEndHour(), range.getEndMin(),
					range.getEndSec())));

			timeRangeElement.appendChild(doc.createTextNode("\n" + offsetString
					+ "\t\t\t"));
		}
		timeRangesElement.appendChild(doc.createTextNode("\n" + offsetString
				+ "\t\t"));

		Set<Integer> dowsList = spec.getDows();
		if (dowsList != null && dowsList.size() > 0) {
			timeSpec.appendChild(doc.createTextNode("\n" + offsetString
					+ "\t\t"));

			Element dowsElement = doc.createElement("dows");
			timeSpec.appendChild(dowsElement);
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (int dow : dowsList) {
				if (i > 0)
					sb.append(',');
				sb.append(Integer.toString(dow));
				i++;
			}
			dowsElement.appendChild(doc.createTextNode(sb.toString()));
		}
		timeSpec.appendChild(doc.createTextNode("\n" + offsetString + "\t"));

		return timeSpec;
	}

}
