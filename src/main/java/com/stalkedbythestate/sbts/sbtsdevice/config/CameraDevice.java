package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.SortedSet;

public interface CameraDevice extends Comparable<CameraDevice> {

	public String getName();
	
	public void setName(String name);
	
	public int getIndex();

	public void setIndex(int index);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
	
	public String getDescription();
	
	public void setDescription(String description);

	public String getUrl();

	public void setUrl(String url);

	public String getUsername();

	public void setUsername(String username);

	public String getPassword();

	public void setPassword(String password);
	
	public int getContinueSeconds();

	public void setContinueSeconds(int continueSeconds);

	public int getBufferSeconds();

	public void setBufferSeconds(int bufferSeconds);
	
	public int getFramesPerSecond();

	public void setFramesPerSecond(int framesPerSecond);

	public int getPriority();

	public void setPriority(int priority);
	
	public SortedSet<CameraDevice> getCameraList();

	public boolean isCachingAllowed();

	public void setCachingAllowed(boolean cachingAllowed);
	
	public boolean isGuest();

	public void setGuest(boolean guest);
	
	public boolean isUp();

	public void setUp(boolean up);
	
	int compareTo(CameraDevice o);
}
