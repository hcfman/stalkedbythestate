package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;

import java.util.*;

public class ActionsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private List<String> actionTypeNames = ActionType.getActionTypeList();
	private List<String> availableEventNames;
	private List<String> availableCameras;
	private Map<String, SortedSet<Integer>> availableRemoteCameras = new HashMap<String, SortedSet<Integer>>();
	private List<String> availableProfiles;
	private List<String> availableTagNames;
	private List<String> availableFreakNames;
	private List<String> availableButtonGroups;
	private List<String> availablePhidgets;
	private List<String> availableRfxcomCommands;
	private Map<String, Set<String>> fieldUsage = new HashMap<String, Set<String>>();
	private List<ActionJSON> actions = new ArrayList<ActionJSON>();
	
	public ActionsJSON() {
	}

	public ActionsJSON(boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getActionTypeNames() {
		return actionTypeNames;
	}

	public void setActionTypeNames(List<String> actionTypeNames) {
		this.actionTypeNames = actionTypeNames;
	}

	public List<ActionJSON> getActions() {
		return actions;
	}

	public void setActions(List<ActionJSON> actions) {
		this.actions = actions;
	}

	public List<String> getAvailableEventNames() {
		return availableEventNames;
	}

	public void setAvailableEventNames(List<String> availableEventNames) {
		this.availableEventNames = availableEventNames;
	}

	public List<String> getAvailableCameras() {
		return availableCameras;
	}

	public void setAvailableCameras(List<String> availableCameras) {
		this.availableCameras = availableCameras;
	}

	public List<String> getAvailableProfiles() {
		return availableProfiles;
	}

	public void setAvailableProfiles(List<String> availableProfiles) {
		this.availableProfiles = availableProfiles;
	}

	public List<String> getAvailableTagNames() {
		return availableTagNames;
	}

	public void setAvailableTagNames(List<String> availableTagNames) {
		this.availableTagNames = availableTagNames;
	}

	public List<String> getAvailableFreakNames() {
		return availableFreakNames;
	}

	public void setAvailableFreakNames(List<String> availableFreakNames) {
		this.availableFreakNames = availableFreakNames;
	}

	public List<String> getAvailablePhidgets() {
		return availablePhidgets;
	}

	public void setAvailablePhidgets(List<String> availablePhidgets) {
		this.availablePhidgets = availablePhidgets;
	}

	public Map<String, Set<String>> getFieldUsage() {
		return fieldUsage;
	}

	public void setFieldUsage(Map<String, Set<String>> fieldUsage) {
		this.fieldUsage = fieldUsage;
	}

	public Map<String, SortedSet<Integer>> getAvailableRemoteCameras() {
		return availableRemoteCameras;
	}

	public void setAvailableRemoteCameras(
			Map<String, SortedSet<Integer>> availableRemoteCameras) {
		this.availableRemoteCameras = availableRemoteCameras;
	}

	public List<String> getAvailableRfxcomCommands() {
		return availableRfxcomCommands;
	}

	public void setAvailableRfxcomCommands(List<String> availableRfxcomCommands) {
		this.availableRfxcomCommands = availableRfxcomCommands;
	}

	public List<String> getAvailableButtonGroups() {
		return availableButtonGroups;
	}

	public void setAvailableButtonGroups(List<String> availableButtonGroups) {
		this.availableButtonGroups = availableButtonGroups;
	}

	public List<Action> toActionList() {
		List<Action> actionList = new ArrayList<Action>();
		for (ActionJSON actionJSON : actions) {
			Action action = null;

			switch (actionJSON.getActionTypeValue()) {
			case ACTION_ADD_TAG:
			case ACTION_DELETE_TAG:
				TagActionImpl tagActionImpl =
					new TagActionImpl(actionJSON.getName(),
						actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getTagActionType(), actionJSON.getTagName());

				actionJSON.setValidFor(tagActionImpl.getValidFor());
				
				action = tagActionImpl;
				break;
			case ACTION_EMAIL:
				EmailActionImpl emailActionImpl = new EmailActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getTo());

				emailActionImpl.setCameraSet(actionJSON.getCameraSet());
				emailActionImpl.setResponseGroup(actionJSON.getResponseGroup());
				
				action = emailActionImpl;
				break;
			case ACTION_SEND_HTTP:
				HttpActionImpl httpActionImpl =
					new HttpActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(),
							actionJSON.getUrl(), actionJSON.getMethodType(), actionJSON.isVerifyHostname(), actionJSON.getUsername(), actionJSON.getPassword());
				httpActionImpl.setParameters(actionJSON.getParameters());
				action = httpActionImpl;
				break;
			case ACTION_VIDEO:
				VideoActionImpl videoActionImpl = new VideoActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getCameraSet());

				action = videoActionImpl;
				break;
			case ACTION_REMOTE_VIDEO:
				RemoteVideoActionImpl remoteVideoActionImpl = new RemoteVideoActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getCameraSet(), actionJSON.getFreakName());

				action = remoteVideoActionImpl;
				break;
			case ACTION_CANCEL_ACTION:
				CancelActionActionImpl cancelActionActionImpl = new CancelActionActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getActionName());
				
				action = cancelActionActionImpl;
				break;
			case ACTION_WEB_PREFIX:
				WebPrefixActionImpl webPrefixActionImpl = new WebPrefixActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getPrefix());
				
				action = webPrefixActionImpl;
				break;
			case ACTION_PHIDGET_OUTPUT:
				PhidgetActionImpl phidgetActionImpl =
					new PhidgetActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(),
						actionJSON.getPhidgetName(), actionJSON.getPort(), actionJSON.getPhidgetActionType());
				
				actionJSON.setPulseTrain(actionJSON.getPulseTrain());
				
				action = phidgetActionImpl;
				break;
			case ACTION_RFXCOM:
				RfxcomActionImpl rfxcomActionImpl = new RfxcomActionImpl(actionJSON.getName(), actionJSON.getEventName(), actionJSON.getDescription(), actionJSON.getRfxcomCommand());
				action = rfxcomActionImpl;
				break;
			}
			
			action.setHysteresis(actionJSON.getHysteresis());
			action.setValidTimes(actionJSON.getValidTimes());
			action.setProfiles(actionJSON.getProfiles());
			action.setPositiveTagNames(actionJSON.getPositiveTagNames());
			action.setNegativeTagNames(actionJSON.getNegativeTagNames());
			
			actionList.add(action);
		}
		return actionList;
	}

	@Override
	public String toString() {
		return "ActionsJSON [result=" + result + ", messages=" + messages
				+ ", actionTypeNames=" + actionTypeNames
				+ ", availableEventNames=" + availableEventNames
				+ ", availableCameras=" + availableCameras
				+ ", availableRemoteCameras=" + availableRemoteCameras
				+ ", availableProfiles=" + availableProfiles
				+ ", availableTagNames=" + availableTagNames
				+ ", availableFreakNames=" + availableFreakNames
				+ ", availableButtonGroups=" + availableButtonGroups
				+ ", availablePhidgets=" + availablePhidgets
				+ ", availableRfxcomCommands=" + availableRfxcomCommands
				+ ", fieldUsage="
				+ fieldUsage
				+ ", actions=" + actions + "]";
	}

}
