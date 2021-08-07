package com.stalkedbythestate.sbts.sbtsdevice.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class FreakConfig {
	private Map<String, FreakDevice> freakMap = new HashMap<String, FreakDevice>();

	public Map<String, FreakDevice> getFreakMap() {
		return freakMap;
	}

	public void setFreakMap(Map<String, FreakDevice> freakMap) {
		this.freakMap = freakMap;
	}
	
	public FreakDevice get(String name) {
		return freakMap.get(name);
	}
	
	public void set(FreakDevice freakDevice) {
		freakMap.put(freakDevice.getName(), freakDevice);
	}

	public void addFreakConfig(Document doc, Element parent) {
		Element linkedFreaksElement = doc.createElement("linkedFreaks");
		parent.appendChild(linkedFreaksElement);
		
		parent.appendChild(doc.createTextNode("\n\n"));

		if (freakMap.isEmpty())
			return;

		for (FreakDevice freakDevice : freakMap.values()) {
			linkedFreaksElement.appendChild(doc.createTextNode("\n\t"));
			Element freakElement = doc.createElement("freak");
			linkedFreaksElement.appendChild(freakElement);
			freakElement.setAttribute("name", freakDevice.getName());
			freakElement.setAttribute("description", freakDevice.getDescription());
			freakElement.setAttribute("hostname", freakDevice.getHostname());
			freakElement.setAttribute("port", Integer.toString(freakDevice.getPort()));
			freakElement.setAttribute("protocol", freakDevice.getProtocol().toString());
			freakElement.setAttribute("verifyHostname", Boolean.toString(freakDevice.isVerifyHostname()));
			freakElement.setAttribute("username", freakDevice.getUsername());
			freakElement.setAttribute("password", freakDevice.getPassword());
			freakElement.setAttribute("guest", Boolean.toString(freakDevice.isGuest()));
			linkedFreaksElement.appendChild(doc.createTextNode("\n"));
		}
	}
	
}
