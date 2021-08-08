package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.Link;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class LinksConfig {
	private volatile List<Link> linkList = new ArrayList<Link>();

	public List<Link> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Link> linkList) {
		this.linkList = linkList;
	}
	
	public void addLinksConfig(Document doc, Element parent) {
		Element links = doc.createElement("links");
		for (Link link : linkList) {
			links.appendChild(doc.createTextNode("\n\t"));
			Element linkElement = doc.createElement("link");
			links.appendChild(linkElement);
			linkElement.setAttribute("name", link.getName());
			
			linkElement.appendChild(doc.createTextNode(link.getLink()));
		}
		links.appendChild(doc.createTextNode("\n"));
		parent.appendChild(links);
		parent.appendChild(doc.createTextNode("\n\n"));
	}

	@Override
	public String toString() {
		return "LinksConfig [linkList=" + linkList + "]";
	}
}
