package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;

public class FreakDeviceImpl implements FreakDevice {
	private String name;
	private String description;
	private String hostname;
	private int port;
	private ProtocolType protocol;
	private boolean verifyHostname;
	private String username;
	private String password;
	private boolean guest = false;

	public FreakDeviceImpl() {
	}

	public FreakDeviceImpl(String name, String description, String hostname,
                           int port, String username, String password, ProtocolType protocol, boolean verifyHostname, boolean guest) {
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.port = port;
		this.protocol = protocol;
		this.verifyHostname = verifyHostname;
		this.username = username;
		this.password = password;
		this.guest = guest;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProtocolType getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolType protocol) {
		this.protocol = protocol;
	}

	public boolean isVerifyHostname() {
		return verifyHostname;
	}

	public void setVerifyHostname(boolean verifyHostname) {
		this.verifyHostname = verifyHostname;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	@Override
	public String toString() {
		return "FreakDeviceImpl [description=" + description + ", guest="
				+ guest + ", hostname=" + hostname + ", name=" + name
				+ ", password=" + password + ", port=" + port + ", protocol="
				+ protocol + ", username=" + username + ", verifyHostname="
				+ verifyHostname + "]";
	}

}
