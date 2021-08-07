package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetActionType;

public class PhidgetActionImpl extends AbstractAction {
	private String phidgetName;
	private int port = 0;
	private PhidgetActionType phidgetActionType = PhidgetActionType.On;
	private String pulseTrain = null;

	public PhidgetActionImpl(String name, String eventName, String description, String phidgetName, int port,
                             PhidgetActionType phidgetActionType, String pulseTrain) {
		setName(name);
		setEventName(eventName);
		setDescription(description);
		setActionType(ActionType.ACTION_PHIDGET_OUTPUT);
		
		this.phidgetName = phidgetName;
		this.port = port;
		this.phidgetActionType = phidgetActionType;
		this.pulseTrain = pulseTrain;
	}

	public PhidgetActionImpl(String name, String eventName, String description, String phidgetName, int port,
                             PhidgetActionType phidgetActionType) {
		setName(name);
		setEventName(eventName);
		setDescription(description);
		setActionType(ActionType.ACTION_PHIDGET_OUTPUT);

		this.phidgetName = phidgetName;
		this.port = port;
		this.phidgetActionType = phidgetActionType;
	}

	public String getPhidgetName() {
		return phidgetName;
	}

	public void setPhidgetName(String phidgetName) {
		this.phidgetName = phidgetName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public PhidgetActionType getPhidgetActionType() {
		return phidgetActionType;
	}

	public void setPhidgetActionType(PhidgetActionType phidgetActionType) {
		this.phidgetActionType = phidgetActionType;
	}

	public String getPulseTrain() {
		return pulseTrain;
	}

	public void setPulseTrain(String pulseTrain) {
		this.pulseTrain = pulseTrain;
	}

}
