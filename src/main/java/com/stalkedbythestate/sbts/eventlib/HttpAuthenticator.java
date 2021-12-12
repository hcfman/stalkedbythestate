package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class HttpAuthenticator extends Authenticator {
	private static HttpAuthenticator instance = null;
	private static final Logger logger = LoggerFactory.getLogger(HttpAuthenticator.class);
	private ThreadLocal<String> username = new ThreadLocal<String>();
	private ThreadLocal<String> password = new ThreadLocal<String>();

	private HttpAuthenticator() {
	}
	
	public static HttpAuthenticator getInstance() {
		if (instance == null)
			instance = new HttpAuthenticator();
		
		Authenticator.setDefault(instance);
		
		return instance;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		if (logger.isDebugEnabled()) {
			logger.debug("username: " + username.get());
			logger.debug("password: " + password.get());
		}
		
		return new PasswordAuthentication(username.get(), password.get().toCharArray());
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

}
