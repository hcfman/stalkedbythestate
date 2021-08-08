package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

public class Link {
	private String name;
	private String link;

	public Link() {
	}

	public Link(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Link [link=" + link + ", name=" + name + "]";
	}

}
