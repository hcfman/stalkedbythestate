package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	private final String username;
	private final String password;
	
	public MyAuthenticator(String username, String password ) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password.toCharArray());
	}

}
