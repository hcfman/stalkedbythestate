package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class UserJSON {
	private String name;
	private String role;
	private String password;

	public UserJSON() {
	}

	public UserJSON(String name, String role, String password) {
		this.name = name;
		this.role = role;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserJSON [name=" + name + ", password=" + password + ", role="
				+ role + "]";
	}

}
