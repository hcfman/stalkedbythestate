package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public interface EmailProvider {
	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getMailhost();

	public void setMailhost(String mailhost);

	public String getFrom();

	public void setFrom(String from);

	public int getPort();

	public void setPort(int port);

	public String getUsername();

	public void setUsername(String username);

	public String getPassword();

	public void setPassword(String password);
	
	public EncryptionType getEncryptionType();

	public void setEncryptionType(EncryptionType encryptionType);
}
