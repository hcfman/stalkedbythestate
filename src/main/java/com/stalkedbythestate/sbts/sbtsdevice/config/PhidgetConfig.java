package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class PhidgetConfig {
	private Map<String, PhidgetDevice> phidgetMap = new HashMap<String, PhidgetDevice>();

	public Map<String, PhidgetDevice> getPhidgetMap() {
		return phidgetMap;
	}

	public void setPhidgetMap(Map<String, PhidgetDevice> phidgetMap) {
		this.phidgetMap = phidgetMap;
	}
	
	public PhidgetDevice get(String name) {
		return phidgetMap.get(name);
	}
	
	public void set(PhidgetDevice phidgetDevice) {
		phidgetMap.put(phidgetDevice.getName(), phidgetDevice);
	}

	public void addPhidgetConfig(Document doc, Element parent) {
		Element phidgetsElement = doc.createElement("phidgets");
		parent.appendChild(phidgetsElement);
		
		parent.appendChild(doc.createTextNode("\n\n"));

		if (phidgetMap.isEmpty())
			return;

		for (PhidgetDevice phidgetDevice : phidgetMap.values()) {
			phidgetsElement.appendChild(doc.createTextNode("\n\t"));
			Element phidgetElement = doc.createElement("phidget");
			phidgetsElement.appendChild(phidgetElement);
			phidgetElement.setAttribute("name", phidgetDevice.getName());
			phidgetElement.setAttribute("description", phidgetDevice.getDescription());
			phidgetElement.setAttribute("serialNumber", Integer.toString(phidgetDevice.getSerialNumber()));
			phidgetElement.setAttribute("portSize", Integer.toString(phidgetDevice.getPortSize()));
			phidgetElement.appendChild(doc.createTextNode("\n\t\t"));
			
			Element inputPortList = doc.createElement("inputPorts");
			phidgetElement.appendChild(inputPortList);
			for (int i = 0; i < PhidgetConstants.PHIDGET_PORT_SIZE; i++) {
				inputPortList.appendChild(doc.createTextNode("\n\t\t\t"));
				boolean value = phidgetDevice.getInitialInputState()[i];
				Element portElement = doc.createElement("port");
				inputPortList.appendChild(portElement);
				portElement.setAttribute("index", Integer.toString(i));
				portElement.appendChild(doc.createTextNode(Boolean.toString(value)));
				
				if (phidgetDevice.getOnTriggerEventNames()[i] != null)
					portElement.setAttribute("onTriggerEventName", phidgetDevice.getOnTriggerEventNames()[i]);
				
				if (phidgetDevice.getOffTriggerEventNames()[i] != null)
					portElement.setAttribute("offTriggerEventName", phidgetDevice.getOffTriggerEventNames()[i]);
			}
			inputPortList.appendChild(doc.createTextNode("\n\t\t"));
			
			phidgetElement.appendChild(doc.createTextNode("\n\t\t"));
			Element outputPortList = doc.createElement("outputPorts");
			phidgetElement.appendChild(outputPortList);
			for (int i = 0; i < PhidgetConstants.PHIDGET_PORT_SIZE; i++) {
				outputPortList.appendChild(doc.createTextNode("\n\t\t\t"));
				boolean value = phidgetDevice.getInitialOutputState()[i];
				Element portElement = doc.createElement("port");
				outputPortList.appendChild(portElement);
				portElement.setAttribute("index", Integer.toString(i));
				portElement.appendChild(doc.createTextNode(Boolean.toString(value)));
			}
			outputPortList.appendChild(doc.createTextNode("\n\t\t"));
			phidgetElement.appendChild(doc.createTextNode("\n\t"));
			
			
			
			phidgetsElement.appendChild(doc.createTextNode("\n"));
		}
	}
	
}
