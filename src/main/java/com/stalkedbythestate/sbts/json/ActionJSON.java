package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.sbtsdevice.config.*;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.apache.log4j.Logger;

import java.util.*;

public class ActionJSON {
	private static final Logger logger = Logger.getLogger(ActionJSON.class);
	private String name;
	private String eventName;
	private String description;
	private String actionType;
	private boolean guest;
	private int delay;
	private String delayUnits;
	private String hysteresisUnits;
	private int hysteresis;
	private EventCounter eventCounter;
	private List<TimeSpec> validTimes;
	private SortedSet<String> profiles = new TreeSet<String>();
	private SortedSet<String> positiveTagNames = new TreeSet<String>();
	private String andMode;
	private SortedSet<String> negativeTagNames = new TreeSet<String>();

	// E-mail specifics
	private String to;
	private String responseGroup;
	
	// E-mail, Video
	private SortedSet<Integer> cameraSet = new TreeSet<Integer>();
	
	// HTTP specifics
	private String url;
	private MethodType methodType;
	private boolean verifyHostname;
	private String username;
	private String password;
	private Map<String, String> parameters = new HashMap<String, String>();
	
	// Tag specifics
	private TagActionType tagActionType;
	private String tagName;
	private Long validFor;
	
	private VideoType videoType;
	
	// Remote video specifics
	private String freakName;
	
	// Cancel Action specifics
	private String actionName;
	
	// Webprefix action specific
	private String prefix;
	
	// Phidget output specific
	private String phidgetName;
	private PhidgetActionType phidgetActionType;
	private int port;
	private String pulseTrain;
	
	// RFXCOM specifics
	private String rfxcomCommand;
	
	public ActionJSON() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public List<TimeSpec> getValidTimes() {
		return validTimes;
	}

	public void setValidTimes(List<TimeSpec> validTimes) {
		this.validTimes = validTimes;
	}

	public SortedSet<String> getPositiveTagNames() {
		return positiveTagNames;
	}

	public void setPositiveTagNames(SortedSet<String> positiveTagNames) {
		this.positiveTagNames = positiveTagNames;
	}

	public SortedSet<String> getNegativeTagNames() {
		return negativeTagNames;
	}

	public void setNegativeTagNames(SortedSet<String> negativeTagNames) {
		this.negativeTagNames = negativeTagNames;
	}

	public String getResponseGroup() {
		return responseGroup;
	}

	public void setResponseGroup(String responseGroup) {
		this.responseGroup = responseGroup;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public SortedSet<Integer> getCameraSet() {
		return cameraSet;
	}

	public void setCameraSet(SortedSet<Integer> cameraSet) {
		this.cameraSet = cameraSet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	public boolean isVerifyHostname() {
		return verifyHostname;
	}

	public void setVerifyHostname(boolean verifyHostname) {
		this.verifyHostname = verifyHostname;
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

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public TagActionType getTagActionType() {
		return tagActionType;
	}

	public void setTagActionType(TagActionType tagActionType) {
		this.tagActionType = tagActionType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Long getValidFor() {
		return validFor;
	}

	public void setValidFor(Long validFor) {
		this.validFor = validFor;
	}

	public VideoType getVideoType() {
		return videoType;
	}

	public void setVideoType(VideoType videoType) {
		this.videoType = videoType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public SortedSet<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(SortedSet<String> profiles) {
		this.profiles = profiles;
	}

	public int getHysteresis() {
		return hysteresis;
	}

	public void setHysteresis(int hysteresis) {
		this.hysteresis = hysteresis;
	}

	public String getHysteresisUnits() {
		return hysteresisUnits;
	}

	public void setHysteresisUnits(String hysteresisUnits) {
		this.hysteresisUnits = hysteresisUnits;
	}

	public EventCounter getEventCounter() {
		return eventCounter;
	}

	public void setEventCounter(EventCounter eventCounter) {
		this.eventCounter = eventCounter;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getDelayUnits() {
		return delayUnits;
	}

	public void setDelayUnits(String delayUnits) {
		this.delayUnits = delayUnits;
	}

	public String getFreakName() {
		return freakName;
	}

	public void setFreakName(String freakName) {
		this.freakName = freakName;
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

	public String getPulseTrain() {
		return pulseTrain;
	}

	public void setPulseTrain(String pulseTrain) {
		this.pulseTrain = pulseTrain;
	}

	public PhidgetActionType getPhidgetActionType() {
		return phidgetActionType;
	}

	public void setPhidgetActionType(PhidgetActionType phidgetActionType) {
		this.phidgetActionType = phidgetActionType;
	}

	public String getRfxcomCommand() {
		return rfxcomCommand;
	}

	public void setRfxcomCommand(String rfxcomCommand) {
		this.rfxcomCommand = rfxcomCommand;
	}

	public ActionType getActionTypeValue() {
		if (logger.isDebugEnabled()) logger.debug("Want to do getActionTypeValue for : " + actionType);
		ActionType returnActionType = null;
		if (actionType.equals("ACTION_VIDEO"))
			returnActionType = ActionType.ACTION_VIDEO;
		else if (actionType.equals("ACTION_EMAIL"))
			returnActionType = ActionType.ACTION_EMAIL;
		else if (actionType.equals("ACTION_ADD_TAG"))
			returnActionType = ActionType.ACTION_ADD_TAG;
		else if (actionType.equals("ACTION_DELETE_TAG"))
			returnActionType = ActionType.ACTION_DELETE_TAG;
		else if (actionType.equals("ACTION_SEND_HTTP"))
			returnActionType = ActionType.ACTION_SEND_HTTP;
		else if (actionType.equals("ACTION_REMOTE_VIDEO"))
			returnActionType = ActionType.ACTION_REMOTE_VIDEO;
		else if (actionType.equals("ACTION_CANCEL_ACTION"))
			returnActionType = ActionType.ACTION_CANCEL_ACTION;
		else if (actionType.equals("ACTION_WEB_PREFIX"))
			returnActionType = ActionType.ACTION_WEB_PREFIX;
		else if (actionType.equals("ACTION_PHIDGET_OUTPUT"))
			returnActionType = ActionType.ACTION_PHIDGET_OUTPUT;	
		else if (actionType.equals("ACTION_RFXCOM"))
			returnActionType = ActionType.ACTION_RFXCOM;
		return returnActionType;
	}
	
	public Action toAction() {
		Action action = null;
		VideoType vType = null;

		if (logger.isDebugEnabled()) logger.debug("In toAction: actionType: " + actionType);
		actionType = ("ACTION_" + actionType).replaceAll(" ", "_");
		if (logger.isDebugEnabled()) logger.debug("Convert ActionJSON to Action: " + toString());

		ActionType actionType = getActionTypeValue();
		switch (actionType) {
		case ACTION_ADD_TAG:
		case ACTION_DELETE_TAG:
			if (actionType == ActionType.ACTION_ADD_TAG)
				setTagActionType(TagActionType.SET_ON);
			else
				setTagActionType(TagActionType.SET_OFF);
				
			TagActionImpl tagActionImpl =
				new TagActionImpl(getName(),
					getEventName(), getDescription(), getTagActionType(), getTagName());

			tagActionImpl.setValidFor(getValidFor());
			
			action = tagActionImpl;
			break;
		case ACTION_EMAIL:
			EmailActionImpl emailActionImpl = new EmailActionImpl(getName(), getEventName(), getDescription(), getTo());
			emailActionImpl.setTo(getTo());
			vType = getVideoType();
			if (vType == null)
				vType = VideoType.MJPEG;
			emailActionImpl.setVideoType(vType);
			emailActionImpl.setResponseGroup(responseGroup);
			
			if (getCameraSet() != null)
				emailActionImpl.setCameraSet(getCameraSet());
			
			action = emailActionImpl;
			break;
		case ACTION_SEND_HTTP:
			HttpActionImpl httpActionImpl =
				new HttpActionImpl(getName(), getEventName(), getDescription(),
						getUrl(), getMethodType(), isVerifyHostname(), getUsername(), getPassword());
			if (getParameters() != null)
				httpActionImpl.setParameters(getParameters());
			
			action = httpActionImpl;
			break;
		case ACTION_VIDEO:
			VideoActionImpl videoActionImpl = new VideoActionImpl(getName(), getEventName(), getDescription(), getCameraSet());

			action = videoActionImpl;
			break;
		case ACTION_REMOTE_VIDEO:
			RemoteVideoActionImpl remoteVideoActionImpl = new RemoteVideoActionImpl(getName(), getEventName(), getDescription(), getCameraSet(), getFreakName());

			action = remoteVideoActionImpl;
			break;
		case ACTION_CANCEL_ACTION:
			CancelActionActionImpl cancelActionActionImpl = new CancelActionActionImpl(getName(), getEventName(), getDescription(), getActionName());
			
			action = cancelActionActionImpl;
			break;
		case ACTION_WEB_PREFIX:
			WebPrefixActionImpl webPrefixActionImpl = new WebPrefixActionImpl(getName(), getEventName(), getDescription(), getPrefix());
			webPrefixActionImpl.setPrefix(getPrefix());
			
			action = webPrefixActionImpl;
			break;
		case ACTION_PHIDGET_OUTPUT:
			PhidgetActionImpl phidgetActionImpl =
				new PhidgetActionImpl(getName(), getEventName(), getDescription(),
					getPhidgetName(), getPort(), getPhidgetActionType());
			
			phidgetActionImpl.setPulseTrain(getPulseTrain());
			
			action = phidgetActionImpl;
			break;
		case ACTION_RFXCOM:
			RfxcomActionImpl rfxcomActionImpl = new RfxcomActionImpl(getName(), getEventName(), getDescription(), rfxcomCommand);
			action = rfxcomActionImpl;
			break;
		}
		
		action.setHysteresis(getHysteresis());

		UnitType newHysteresisUnits = UnitType.sec;
		if (getHysteresisUnits() != null)
			newHysteresisUnits = UnitType.set(getHysteresisUnits());
		action.setHysteresisUnits(newHysteresisUnits);
		action.setEventcounter(getEventCounter());

		action.setDelayFor(getDelay());
		
		UnitType newDelayUnits = UnitType.sec;
		if (getDelayUnits() != null)
			newDelayUnits = UnitType.set(getDelayUnits());
		action.setDelayUnits(newDelayUnits);
		
		action.setGuest(isGuest());
		
		if (logger.isDebugEnabled()) logger.debug("Action name: " + getName());
		if (getValidTimes() != null) {
			if (logger.isDebugEnabled()) logger.debug("There are validTimes");
			action.setValidTimes(getValidTimes());
		} else {
			if (logger.isDebugEnabled()) logger.debug("There are no validTimes");
		}
		
		if (getProfiles() != null)
			action.setProfiles(getProfiles());
		
		if (getPositiveTagNames() != null)
			action.setPositiveTagNames(getPositiveTagNames());
		boolean andModeBoolean = false;
		try {
			andModeBoolean = Boolean.parseBoolean(andMode);
		} catch (Exception e) {
			logger.error("Can't parse andMode string to boolean");
		}
		action.setPositiveTagAndMode(andModeBoolean);
		
		if (getNegativeTagNames() != null)
			action.setNegativeTagNames(getNegativeTagNames());
		
		return action;
	}

	public String getAndMode() {
		return andMode;
	}

	public void setAndMode(String andMode) {
		this.andMode = andMode;
	}

	@Override
	public String toString() {
		return "ActionJSON [name=" + name + ", eventName=" + eventName
				+ ", description=" + description + ", actionType=" + actionType
				+ ", guest=" + guest + ", delay=" + delay + ", delayUnits="
				+ delayUnits + ", hysteresisUnits=" + hysteresisUnits
				+ ", hysteresis=" + hysteresis + ", eventCounter="
				+ eventCounter + ", validTimes=" + validTimes + ", profiles="
				+ profiles + ", positiveTagNames=" + positiveTagNames
				+ ", andMode=" + andMode + ", negativeTagNames="
				+ negativeTagNames + ", to=" + to + ", responseGroup="
				+ responseGroup + ", cameraSet=" + cameraSet + ", url=" + url
				+ ", methodType=" + methodType + ", verifyHostname="
				+ verifyHostname + ", username=" + username + ", password="
				+ password + ", parameters=" + parameters + ",tagActionType="
				+ tagActionType + ", tagName=" + tagName + ", validFor="
				+ validFor + ", videoType="
				+ videoType + ", freakName=" + freakName + ", actionName="
				+ actionName + ", prefix=" + prefix + ", phidgetName="
				+ phidgetName + ", phidgetActionType=" + phidgetActionType
				+ ", port=" + port + ", pulseTrain=" + pulseTrain
				+ ", rfxcomCommand=" + rfxcomCommand + "]";
	}
	
}
