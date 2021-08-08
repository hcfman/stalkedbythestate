package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public class UsersJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	
	private List<UserJSON> users = new ArrayList<UserJSON>();

	public UsersJSON() {
	}

	public UsersJSON(boolean result) {
		this.result = result;
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

	public List<UserJSON> getUsers() {
		return users;
	}

	public void setUsers(List<UserJSON> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "UsersJSON [messages=" + messages + ", result=" + result
				+ ", users=" + users + "]";
	}
	
}
