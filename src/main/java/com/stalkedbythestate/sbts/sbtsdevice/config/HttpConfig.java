package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpConfig {
	private volatile Map<String, List<HttpTrigger>> groupMap = new ConcurrentHashMap<String, List<HttpTrigger>>();

	public Map<String, List<HttpTrigger>> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, List<HttpTrigger>> groupMap) {
		this.groupMap = groupMap;
	}

	public List<String> getGroupsAsList() {
		List<String> groupList = new ArrayList<String>(getGroupMap().keySet());
		Collections.sort(groupList);

		return groupList;
	}

	public void addHttpConfig(Document doc, Element parent) {
		Element httpElement = doc.createElement("http");
		parent.appendChild(doc.createTextNode("\n"));

		parent.appendChild(httpElement);

		for (String groupName : groupMap.keySet()) {
			httpElement.appendChild(doc.createTextNode("\n\t"));
			Element groupNameElement = doc.createElement("group");
			httpElement.appendChild(groupNameElement);
			groupNameElement.setAttribute("name", groupName);

			for (HttpTrigger httpTrigger : groupMap.get(groupName)) {
				groupNameElement.appendChild(doc.createTextNode("\n\t\t"));
				Element httpTriggerElement = doc.createElement("httpTrigger");
				groupNameElement.appendChild(httpTriggerElement);

				httpTriggerElement.setAttribute("eventName",
						httpTrigger.getEventName());
				httpTriggerElement.setAttribute("description",
						httpTrigger.getDescription());
				httpTriggerElement.setAttribute("guest",
						Boolean.toString(httpTrigger.isGuest()));
			}
			groupNameElement.appendChild(doc.createTextNode("\n\t"));
		}
		httpElement.appendChild(doc.createTextNode("\n"));
		parent.appendChild(doc.createTextNode("\n\n"));
	}

	@Override
	public String toString() {
		return "HttpConfig [groupMap=" + groupMap + "]";
	}

}
