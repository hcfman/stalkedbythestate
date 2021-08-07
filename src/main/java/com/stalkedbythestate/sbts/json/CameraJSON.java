package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.CameraDeviceImpl;


public class CameraJSON {
	private String name;
	private int index;
	boolean enabled = true;
	private String description;
	private String url;
	private String username;
	private String password;
	private int continueSeconds;
	private int bufferSeconds;
	private int framesPerSecond = 5;
	private int priority;

	private boolean cachingAllowed = false;
	private boolean guest = false;

	public CameraJSON() {
	}

	
	public CameraDevice toCameraDevice() {
		CameraDevice cameraDevice = new CameraDeviceImpl(name, index, enabled, description, url,
				username, password, continueSeconds, bufferSeconds, framesPerSecond, priority);
		cameraDevice.setCachingAllowed(cachingAllowed);
		cameraDevice.setGuest(guest);
		return cameraDevice;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getContinueSeconds() {
		return continueSeconds;
	}

	public void setContinueSeconds(int continueSeconds) {
		this.continueSeconds = continueSeconds;
	}

	public int getBufferSeconds() {
		return bufferSeconds;
	}

	public void setBufferSeconds(int bufferSeconds) {
		this.bufferSeconds = bufferSeconds;
	}

	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	public void setFramesPerSecond(int framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCachingAllowed() {
		return cachingAllowed;
	}

	public void setCachingAllowed(boolean cachingAllowed) {
		this.cachingAllowed = cachingAllowed;
	}

	public boolean isGuest() {
		return guest;
	}


	public void setGuest(boolean guest) {
		this.guest = guest;
	}


	@Override
	public String toString() {
		return "CameraJSON [bufferSeconds=" + bufferSeconds
				+ ", cachingAllowed=" + cachingAllowed + ", continueSeconds="
				+ continueSeconds + ", description=" + description
				+ ", enabled=" + enabled + ", framesPerSecond="
				+ framesPerSecond + ", guest=" + guest + ", index=" + index
				+ ", name=" + name + ", password=" + password + ", priority="
				+ priority + ", url=" + url + ", username=" + username + "]";
	}

}
