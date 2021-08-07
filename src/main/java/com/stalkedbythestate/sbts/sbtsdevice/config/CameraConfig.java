package com.stalkedbythestate.sbts.sbtsdevice.config;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class CameraConfig {
	private static final Logger logger = Logger.getLogger(CameraConfig.class);
	private volatile Map<Integer, CameraDevice> cameraDevices = new TreeMap<Integer, CameraDevice>();
	private volatile SortedSet<Integer> cameraOrder = new TreeSet<Integer>();

	public CameraDevice get(int index) {
		return cameraDevices.get(index);
	}
	
	public void put(CameraDevice device) {
		if (cameraDevices == null)
			return;
		
		cameraDevices.remove(device.getIndex());
		cameraDevices.put(device.getIndex(), device);
	}
	
	public Map<Integer, CameraDevice> getCameraDevices() {
		return cameraDevices;
	}

	public void setCameraDevices(Map<Integer, CameraDevice> cameraDevices) {
		if (logger.isDebugEnabled()) logger.debug("Setting cameraDevices to cameraConfig");
		this.cameraDevices = cameraDevices;
		cameraOrder = new TreeSet<Integer>();
		
		// so we can access in index order
		for (CameraDevice cameraDevice : cameraDevices.values()) {
			if (logger.isDebugEnabled()) logger.debug("CameraDevice.index: " + cameraDevice.getIndex());
			cameraOrder.add(cameraDevice.getIndex());
		}
	}

	public SortedSet<Integer> getCameraOrder() {
		return cameraOrder;
	}

	public void addCameraConfig(Document doc, Element parent) {
		Element camerasElement = doc.createElement("cameras");
		parent.appendChild(camerasElement);
		if (cameraDevices == null)
			return;

		for (CameraDevice camera : cameraDevices.values()) {
			camerasElement.appendChild(doc.createTextNode("\n\t"));
			Element cameraElement = doc.createElement("camera");
			camerasElement.appendChild(cameraElement);
			cameraElement.setAttribute("name", camera.getName());
			cameraElement.setAttribute("index", Integer.toString(camera.getIndex()));
			cameraElement.setAttribute("enabled", Boolean.toString(camera.isEnabled()));
			cameraElement.setAttribute("description", camera.getDescription());
			cameraElement.setAttribute("url", camera.getUrl());
			cameraElement.setAttribute("username", camera.getUsername());
			cameraElement.setAttribute("password", camera.getPassword());
			cameraElement.setAttribute("continueSeconds", String.valueOf(camera.getContinueSeconds()));
			cameraElement.setAttribute("bufferSeconds", String.valueOf(camera.getBufferSeconds()));
			cameraElement.setAttribute("framesPerSecond", String.valueOf(camera.getFramesPerSecond()));
			cameraElement.setAttribute("priority", String.valueOf(camera.getPriority()));
			cameraElement.setAttribute("cachingAllowed", Boolean.toString(camera.isCachingAllowed()));
			cameraElement.setAttribute("guest", Boolean.toString(camera.isGuest()));
		}
		camerasElement.appendChild(doc.createTextNode("\n"));
		parent.appendChild(doc.createTextNode("\n"));
	}

}
