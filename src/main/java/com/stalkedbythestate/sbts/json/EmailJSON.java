package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.EncryptionType;

import java.util.ArrayList;
import java.util.List;


public class EmailJSON {
	// Status
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	
	// Object fields
	private String name = "";
	private String description = "";
	private String mailhost = "";
	private String from = "";
	private int port = 25;
	private String username;
	private String password;
	private String encType;

	// For the selection box
	private List<String> encryptionTypeNames = EncryptionType.getEncryptionTypeList();

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

	public String getMailhost() {
		return mailhost;
	}

	public void setMailhost(String mailhost) {
		this.mailhost = mailhost;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
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

	public String getEncType() {
		return encType;
	}

	public void setEncType(String encType) {
		this.encType = encType;
	}

	@Override
	public String toString() {
		return "EmailJSON [result=" + result + ", messages=" + messages
				+ ", name=" + name + ", description=" + description
				+ ", mailhost=" + mailhost + ", from=" + from + ", port="
				+ port + ", username=" + username + ", password=" + password
				+ ", encType=" + encType + "]";
	}

}
