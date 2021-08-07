package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.PhidgetDevice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static com.stalkedbythestate.sbts.sbtsdevice.configimpl.PhidgetConstants.PHIDGET_PORT_SIZE;

@Getter(onMethod_={@Synchronized})
@Setter(onMethod_={@Synchronized})
public class PhidgetDeviceImpl implements PhidgetDevice {
	private String name;
	private String description;
	private int serialNumber;
	private int portSize;
	private boolean[] outputState = new boolean[PHIDGET_PORT_SIZE];
	private boolean[] inputState = new boolean[PHIDGET_PORT_SIZE];
	private boolean[] initialInputState = new boolean[PHIDGET_PORT_SIZE];
	private boolean[] initialOutputState = new boolean[PHIDGET_PORT_SIZE];
	private String[] onTriggerEventNames = new String[PHIDGET_PORT_SIZE];
	private String[] offTriggerEventNames = new String[PHIDGET_PORT_SIZE];

	@Getter(AccessLevel.NONE)
	private volatile boolean connected = false;

	private AtomicIntegerArray connectedInputs = new AtomicIntegerArray(PHIDGET_PORT_SIZE);
	private AtomicIntegerArray connectedOutputs = new AtomicIntegerArray(PHIDGET_PORT_SIZE);

	public PhidgetDeviceImpl() {
	}

	public PhidgetDeviceImpl(final String name, final String description,
                             final int serialNumber, final int portSize) {
		this.name = name;
		this.description = description;
		this.serialNumber = serialNumber;
		this.portSize = portSize;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public int getSerialNumber() {
//		return serialNumber;
//	}
//
//	public void setSerialNumber(int serialNumber) {
//		this.serialNumber = serialNumber;
//	}
//
//	public int getPortSize() {
//		return portSize;
//	}
//
//	public void setPortSize(int portSize) {
//		this.portSize = portSize;
//	}
//
//	public boolean[] getOutputState() {
//		return outputState;
//	}
//
//	public void setOutputState(boolean[] outputState) {
//		this.outputState = outputState;
//	}
//
//	public boolean[] getInputState() {
//		return inputState;
//	}
//
//	public void setInputState(boolean[] inputState) {
//		this.inputState = inputState;
//	}
//
//	public boolean[] getInitialOutputState() {
//		return initialOutputState;
//	}
//
//	public void setInitialOutputState(boolean[] initialOutputState) {
//		this.initialOutputState = initialOutputState;
//	}
//
//	public boolean[] getInitialInputState() {
//		return initialInputState;
//	}
//
//	public void setInitialInputState(boolean[] initialInputState) {
//		this.initialInputState = initialInputState;
//	}
//
//	public String[] getOnTriggerEventNames() {
//		return onTriggerEventNames;
//	}
//
//	public String[] getOffTriggerEventNames() {
//		return offTriggerEventNames;
//	}
//
//	public boolean isConnected() {
//		return connected;
//	}
//
//	public void setConnected(boolean connected) {
//		this.connected = connected;
//	}


	public boolean isConnectedInput(final int port) {
		synchronized (connectedInputs) {
			return connectedInputs.get(port) == 1;
		}
	}

	public void setConnectedInput(final int port, final boolean state) {
		synchronized (connectedInputs) {
			connectedInputs.set(port, state ? 1 : 0);
		}
	}

	public boolean isConnectedOutput(final int port) {
		synchronized (connectedOutputs) {
			return connectedOutputs.get(port) == 1;
		}
	}

	public void setConnectedOutput(final int port, final boolean state) {
		synchronized (connectedOutputs) {
			connectedOutputs.set(port, state ? 1 : 0);
		}
	}

	public boolean isConnected() {
		int count = 0;
		for (int i = 0; i < getPortSize(); i++) {
			if (connectedInputs.get(i) == 1) {
				count++;
			}
			if (connectedOutputs.get(i) == 1) {
				count++;
			}
		}
		return count == 2 * getPortSize();
	}

	@Override
	public String toString() {
		return "PhidgetDeviceImpl [name=" + name + ", description="
				+ description + ", serialNumber=" + serialNumber
				+ ", portSize=" + portSize + ", outputState="
				+ Arrays.toString(outputState) + ", inputState="
				+ Arrays.toString(inputState) + ", initialInputState="
				+ Arrays.toString(initialInputState) + ", initialOutputState="
				+ Arrays.toString(initialOutputState)
				+ ", onTriggerEventNames="
				+ Arrays.toString(onTriggerEventNames)
				+ ", offTriggerEventNames="
				+ Arrays.toString(offTriggerEventNames) + ", connected="
				+ connected + "]";
	}

}
