package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;

public class RfxcomActionImpl extends AbstractAction {
	private String rfxcomName;

	public RfxcomActionImpl(String name, String eventName, String description, String rfxcomName) {
		setName(name);
		setEventName(eventName);
		setDescription(description);
		setActionType(ActionType.ACTION_RFXCOM);
		this.rfxcomName = rfxcomName;
	}

	public String getRfxcomName() {
		return rfxcomName;
	}

	public void setRfxcomName(String rfxcomName) {
		this.rfxcomName = rfxcomName;
	}

}
