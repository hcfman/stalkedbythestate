package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class NetworkJSON {
	boolean dhcp;
	private String hostname;
	private String address;
	private String mask;
	private String defaultRoute;
	private String nameServer1;
	private String nameServer2;
	private String nameServer3;
	private String protocolDescriptor;
	private int httpPort;
	private int httpsPort;

	public NetworkJSON() {
	}

	public boolean isDhcp() {
		return dhcp;
	}

	public void setDhcp(boolean dhcp) {
		this.dhcp = dhcp;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(String defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public String getNameServer1() {
		return nameServer1;
	}

	public void setNameServer1(String nameServer1) {
		this.nameServer1 = nameServer1;
	}

	public String getNameServer2() {
		return nameServer2;
	}

	public void setNameServer2(String nameServer2) {
		this.nameServer2 = nameServer2;
	}

	public String getNameServer3() {
		return nameServer3;
	}

	public void setNameServer3(String nameServer3) {
		this.nameServer3 = nameServer3;
	}

	public String getProtocolDescriptor() {
		return protocolDescriptor;
	}

	public void setProtocolDescriptor(String protocolDescriptor) {
		this.protocolDescriptor = protocolDescriptor;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getHttpsPort() {
		return httpsPort;
	}

	public void setHttpsPort(int httpsPort) {
		this.httpsPort = httpsPort;
	}

	@Override
	public String toString() {
		return "NetworkJSON [address=" + address + ", defaultRoute="
				+ defaultRoute + ", dhcp=" + dhcp + ", hostname=" + hostname
				+ ", httpPort=" + httpPort + ", httpsPort=" + httpsPort
				+ ", mask=" + mask + ", nameServer1=" + nameServer1
				+ ", nameServer2=" + nameServer2 + ", nameServer3="
				+ nameServer3 + ", protocolDescriptor=" + protocolDescriptor
				+ "]";
	}

}
