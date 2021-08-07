package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;

import java.util.HashMap;
import java.util.Map;

public class HttpActionImpl extends AbstractAction {
	private String url;
	private MethodType methodType;
	private boolean verifyHostname;
	private String username;
	private String password;
	private Map<String, String> parameters = new HashMap<String, String>();

	public HttpActionImpl(String name, String eventName, String description, String url, MethodType methodType, boolean verifyHostname, String username,
                          String password) {
		setName(name);
		setActionType(ActionType.ACTION_SEND_HTTP);
		setEventName(eventName);
		setDescription(description);
		this.url = url;
		this.methodType = methodType;
		this.verifyHostname = verifyHostname;
		this.username = username;
		this.password = password;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	public boolean isVerifyHostname() {
		return verifyHostname;
	}

	public void setVerifyHostname(boolean verifyHostname) {
		this.verifyHostname = verifyHostname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void put(String key, String value) {
		parameters.put(key, value);
	}
	
	public String get(String key) {
		return parameters.get(key);
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
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

	@Override
	public String toString() {
		return "HttpActionImpl [methodType=" + methodType + ", parameters="
				+ parameters + ", password=" + password + ", url=" + url
				+ ", username=" + username + ", verifyHostname="
				+ verifyHostname + "]";
	}

}
