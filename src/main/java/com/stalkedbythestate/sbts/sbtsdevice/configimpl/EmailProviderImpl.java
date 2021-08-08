package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.EmailProvider;
import com.stalkedbythestate.sbts.sbtsdevice.config.EncryptionType;

public class EmailProviderImpl implements EmailProvider {
	private String name;
	private String description;
	private String mailhost;
	private String from;
	private int port;
	private String username;
	private String password;
	private EncryptionType encryptionType;

	public EmailProviderImpl() {	
	}
	
	public EmailProviderImpl(String name, String description, String mailhost, String from) {
		this.name = name;
		this.description = description;
		this.mailhost = mailhost;
		this.from = from;
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

	@Override
	public String getFrom() {
		return from;
	}

	@Override
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

	public EncryptionType getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(EncryptionType encryptionType) {
		this.encryptionType = encryptionType;
	}
	
}
