package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public enum ActionType {
	ACTION_VIDEO, ACTION_EMAIL, ACTION_ADD_TAG, ACTION_DELETE_TAG, ACTION_SEND_HTTP, ACTION_REMOTE_VIDEO, ACTION_CANCEL_ACTION, ACTION_WEB_PREFIX, ACTION_PHIDGET_OUTPUT, ACTION_RFXCOM;

	public static ActionType set(String value) throws ActionTypeException {
		if (value.equals("ACTION_EMAIL"))
			return ACTION_EMAIL;
		if (value.equals("ACTION_VIDEO"))
			return ACTION_VIDEO;
		if (value.equals("ACTION_ADD_TAG"))
			return ACTION_ADD_TAG;
		if (value.equals("ACTION_DELETE_TAG"))
			return ACTION_DELETE_TAG;
		if (value.equals("ACTION_SEND_HTTP"))
			return ACTION_SEND_HTTP;
		if (value.equals("ACTION_REMOTE_VIDEO"))
			return ACTION_REMOTE_VIDEO;
		if (value.equals("ACTION_CANCEL_ACTION"))
			return ACTION_CANCEL_ACTION;
		if (value.equals("ACTION_WEB_PREFIX"))
			return ACTION_WEB_PREFIX;
		if (value.equals("ACTION_PHIDGET_OUTPUT"))
			return ACTION_PHIDGET_OUTPUT;
		if (value.equals("ACTION_RFXCOM"))
			return ACTION_RFXCOM;
		
		throw new ActionTypeException("Unknown value specified for ActionType Enum: " + value);
	}

	public String getActionTypeName() {
		return this.toString().replaceAll("^ACTION_", "").replaceAll("_", " ");
	}

	public static List<String> getActionTypeList() {
		List<String> actionTypeList = new ArrayList<String>();
		for (ActionType actionType : ActionType.values()) {
			actionTypeList.add(actionType.toString().replaceAll("^ACTION_", "").replaceAll("_", " "));
		}

		return actionTypeList;
	}

}
