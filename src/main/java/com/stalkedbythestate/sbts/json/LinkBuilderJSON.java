package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.Link;

import java.util.*;

public class LinkBuilderJSON {
	private SortedSet<Integer> cameraList = new TreeSet<Integer>();
	private Map<String, SortedSet<Integer>> freakMap = new HashMap<String,SortedSet<Integer>>();
	private List<Link> linkList = new ArrayList<Link>();
	
	private boolean result = false;
	private List<String> messages;
	
	public LinkBuilderJSON() {
	}

	public SortedSet<Integer> getCameraList() {
		return cameraList;
	}

	public void setCameraList(SortedSet<Integer> cameraList) {
		this.cameraList = cameraList;
	}

	public Map<String, SortedSet<Integer>> getFreakMap() {
		return freakMap;
	}

	public void setFreakMap(Map<String, SortedSet<Integer>> freakMap) {
		this.freakMap = freakMap;
	}

	public List<Link> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Link> linkList) {
		this.linkList = linkList;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "LinkBuilderJSON [cameraList=" + cameraList + ", freakMap="
				+ freakMap + ", linkList=" + linkList + ", messages="
				+ messages + ", result=" + result + "]";
	}
	

}
