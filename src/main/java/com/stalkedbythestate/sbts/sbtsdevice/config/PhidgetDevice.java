package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public interface PhidgetDevice {

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public int getSerialNumber();
	
	public int getPortSize();

	public void setSerialNumber(int serialNumber);

	public boolean[] getOutputState();

	public void setOutputState(boolean[] outputState);

	public boolean[] getInputState();

	public void setInputState(boolean[] inputState);

	public boolean[] getInitialOutputState();

	public void setInitialOutputState(boolean[] initialOutputState);
	
	public boolean[] getInitialInputState();

	public void setInitialInputState(boolean[] initialInputState);
	
	public String[] getOnTriggerEventNames();

	public String[] getOffTriggerEventNames();
	
	public boolean isConnected();

	public boolean isConnectedInput(final int port);

	public void setConnectedInput(final int port, final boolean state);

	public boolean isConnectedOutput(final int port);

	public void setConnectedOutput(final int port, final boolean state);

}
