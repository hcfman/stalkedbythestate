package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetConstants;

import java.util.Arrays;

public class PhidgetJSON {
	private String name;
	private String description;
	private String serialNumber;
	private String portSize;
	private boolean initialInputState[] = new boolean[PhidgetConstants.PHIDGET_PORT_SIZE];
	private boolean initialOutputState[] = new boolean[PhidgetConstants.PHIDGET_PORT_SIZE];
	private String onTriggerEventNames[] = new String[PhidgetConstants.PHIDGET_PORT_SIZE];
	private String offTriggerEventNames[] = new String[PhidgetConstants.PHIDGET_PORT_SIZE];

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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPortSize() {
		return portSize;
	}

	public void setPortSize(String portSize) {
		this.portSize = portSize;
	}

	public boolean[] getInitialInputState() {
		return initialInputState;
	}

	public void setInitialInputState(boolean[] initialInputState) {
		this.initialInputState = initialInputState;
	}

	public boolean[] getInitialOutputState() {
		return initialOutputState;
	}

	public void setInitialOutputState(boolean[] initialOutputState) {
		this.initialOutputState = initialOutputState;
	}

	public String[] getOnTriggerEventNames() {
		return onTriggerEventNames;
	}

	public void setOnTriggerEventNames(String[] onTriggerEventNames) {
		this.onTriggerEventNames = onTriggerEventNames;
	}

	public String[] getOffTriggerEventNames() {
		return offTriggerEventNames;
	}

	public void setOffTriggerEventNames(String[] offTriggerEventNames) {
		this.offTriggerEventNames = offTriggerEventNames;
	}

	@Override
	public String toString() {
		return "PhidgetJSON [name=" + name + ", description=" + description
				+ ", serialNumber=" + serialNumber + ", portSize=" + portSize
				+ ", initialInputState=" + Arrays.toString(initialInputState)
				+ ", initialOutputState=" + Arrays.toString(initialOutputState)
				+ ", onTriggerEventNames="
				+ Arrays.toString(onTriggerEventNames)
				+ ", offTriggerEventNames="
				+ Arrays.toString(offTriggerEventNames) + "]";
	}

}
