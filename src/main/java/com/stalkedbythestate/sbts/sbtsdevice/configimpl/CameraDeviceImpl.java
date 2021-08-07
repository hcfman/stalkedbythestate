package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;

import java.util.SortedSet;
import java.util.TreeSet;

public class CameraDeviceImpl implements CameraDevice, Comparable<CameraDevice> {
	private String name;
	private int index;
	private boolean enabled = true;
	private volatile boolean up = false; // Set by the reader;
	private String description;
	private String url;
	private String username;
	private String password;
	private int continueSeconds;
	private int bufferSeconds;
	private int framesPerSecond = 5;
	private int priority = 1;
	private boolean cachingAllowed = false;
	private boolean guest = false;
	private SortedSet<CameraDevice> cameraList = new TreeSet<CameraDevice>();

	public CameraDeviceImpl() {
	}

public CameraDeviceImpl(String name, int index, boolean enabled,
                        String description, String url, String username, String password,
                        int continueSeconds, int bufferSeconds, int framesPerSecond,
                        int priority) {
		this.name = name;
		this.index = index;
		this.enabled = enabled;
		this.description = description;
		this.url = url;
		this.username = username;
		this.password = password;
		this.continueSeconds = continueSeconds;
		this.bufferSeconds = bufferSeconds;
		this.framesPerSecond = framesPerSecond;
		this.priority = priority;
		
		if (name == null)
			name = "cam" + index;
		
		if (description == null)
			description = name;
		
		if (url == null)
			url = "";
		
		if (username == null)
			username = "";
		
		if (password == null)
			password = "";
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

	public int getTotalTime() {
		return continueSeconds;
	}

	public void setTotalTime(int totalTime) {
		this.continueSeconds = totalTime;
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

	public SortedSet<CameraDevice> getCameraList() {
		return cameraList;
	}

	public void setCameraList(SortedSet<CameraDevice> cameraList) {
		this.cameraList = cameraList;
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

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	@Override
	public int compareTo(CameraDevice o) {
		return new Integer(index).compareTo(new Integer(o.getIndex()));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CameraDeviceImpl)) {
			return false;
		}

		if (this == obj)
			return true;

		CameraDevice camDev = (CameraDevice) obj;
		return index == (camDev.getIndex());
	}

	@Override
	public int hashCode() {
		return index;
	}

	@Override
	public String toString() {
		return "CameraDeviceImpl [bufferSeconds=" + bufferSeconds
				+ ", cachingAllowed=" + cachingAllowed + ", cameraList="
				+ cameraList + ", continueSeconds=" + continueSeconds
				+ ", description=" + description + ", enabled=" + enabled
				+ ", framesPerSecond=" + framesPerSecond + ", guest=" + guest
				+ ", index=" + index + ", name=" + name + ", password="
				+ password + ", priority=" + priority + ", url=" + url
				+ ", username=" + username + "]";
	}
	
}
