package com.stalkedbythestate.sbts.rfxcomlib;

// Copyright (c) 2021 Kim Hendrikse

public interface RfxComListener {
	public void fire(Packet packet);
	
	public RfxcomCommand getCommand();
	
	public boolean matches(Packet packet);
}
