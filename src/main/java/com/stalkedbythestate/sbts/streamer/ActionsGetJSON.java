package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ActionJSON;
import com.stalkedbythestate.sbts.json.ActionsJSON;
import com.stalkedbythestate.sbts.json.CameraListJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.FreakDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet(urlPatterns={"/actionsgetjson"})
public class ActionsGetJSON extends HttpServlet {
	private static final long serialVersionUID = -3413744800842427186L;
	private static final Logger logger = Logger.getLogger(ActionsGetJSON.class);
	private SbtsDeviceConfig sbtsConfig;
	private List<RemoteFreakSpec> remoteFreakSpecList;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private void fetchRemoteCamListJSON(Collection<FreakDevice> freakDeviceList) {
		remoteFreakSpecList = new ArrayList<RemoteFreakSpec>();
		if (freakDeviceList.size() <= 0)
			return;

		if (logger.isDebugEnabled())
			logger.debug("About to create executor Service, size: "
					+ freakDeviceList.size());
		ExecutorService executorService = Executors.newFixedThreadPool(
				freakDeviceList.size(), new RemoteCamListThreadFactory());

		for (FreakDevice freakDevice : freakDeviceList) {
			RemoteFreakSpec remoteFreakSpec = new RemoteFreakSpec(freakDevice);
			remoteFreakSpecList.add(remoteFreakSpec);
			if (logger.isDebugEnabled())
				logger.debug("About to try spawning");
			try {
				if (logger.isDebugEnabled())
					logger.debug("Try spawning");
				executorService.execute(new HandleRemoteCamListFetch(
						remoteFreakSpec));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ActionsJSON getActionsJSON() {
		if (logger.isDebugEnabled())
			logger.debug("In getActionsJSON");
		ActionsJSON actionsJSON = new ActionsJSON(true);
		setupActionsJSON(actionsJSON);

		if (logger.isDebugEnabled())
			logger.debug("Setting getAvailableEventNames: "
					+ sbtsConfig.getAvailableEventNames());
		actionsJSON.setAvailableEventNames(sbtsConfig.getAvailableEventNames());
		actionsJSON.setAvailableProfiles(sbtsConfig.getAvailableProfiles());
		actionsJSON.setAvailableTagNames(sbtsConfig.getAvailableTagNames());
		actionsJSON.setAvailableFreakNames(sbtsConfig.getAvailableFreakNames());
		actionsJSON.setAvailableCameras(sbtsConfig.getAvailableCameras());
		actionsJSON.setAvailablePhidgets(sbtsConfig.getAvailablePhidgets());
		actionsJSON.setAvailableRfxcomCommands(sbtsConfig
				.getAvailableRfxcomCommands());
		actionsJSON.setAvailableButtonGroups(sbtsConfig
				.getAvailableButtonGroups());

		fetchRemoteCamListJSON(sbtsConfig.getFreakConfig().getFreakMap()
				.values());

		Action[] actionsArray = sbtsConfig.getActionConfig().getActionList()
				.toArray(new Action[0]);
		Arrays.sort(actionsArray, new ActionsSortOrder());
		for (Action action : actionsArray) {
			ActionJSON actionJSON = new ActionJSON();
			actionJSON
					.setActionType(action.getActionType().getActionTypeName());
			actionJSON.setHysteresis(action.getHysteresis());
			actionJSON.setDelay(action.getDelayFor());
			actionJSON.setDelayUnits(action.getDelayUnits().toString());
			actionJSON.setHysteresisUnits(action.getHysteresisUnits()
					.toString());
			actionJSON.setEventCounter(action.getEventcounter());
			actionJSON.setName(action.getName());
			actionJSON.setEventName(action.getEventName());
			actionJSON.setDescription(action.getDescription());
			actionJSON.setValidTimes(action.getValidTimes());
			actionJSON.setProfiles(action.getProfiles());
			actionJSON.setPositiveTagNames(action.getPositiveTagNames());
			actionJSON.setAndMode(Boolean.toString(action.isPositiveTagAndMode()));
			actionJSON.setNegativeTagNames(action.getNegativeTagNames());
			actionJSON.setGuest(action.isGuest());

			switch (action.getActionType()) {
			case ACTION_ADD_TAG:
			case ACTION_DELETE_TAG:
				TagActionImpl tagActionImpl = (TagActionImpl) action;

				actionJSON.setTagActionType(tagActionImpl.getTagActionType());
				actionJSON.setTagName(tagActionImpl.getTagName());
				actionJSON.setValidFor(tagActionImpl.getValidFor());
				break;
			case ACTION_EMAIL:
				EmailActionImpl emailActionImpl = (EmailActionImpl) action;

				actionJSON.setTo(emailActionImpl.getTo());
				actionJSON.setVideoType(emailActionImpl.getVideoType());
				actionJSON.setCameraSet(emailActionImpl.getCameraSet());
				String responseGroup = emailActionImpl.getResponseGroup();
				if (responseGroup == null)
					responseGroup = "";
				actionJSON.setResponseGroup(responseGroup);
				break;
			case ACTION_SEND_HTTP:
				HttpActionImpl httpActionImpl = (HttpActionImpl) action;

				actionJSON.setUrl(httpActionImpl.getUrl());
				actionJSON.setMethodType(httpActionImpl.getMethodType());
				actionJSON.setVerifyHostname(httpActionImpl.isVerifyHostname());
				actionJSON.setUsername(httpActionImpl.getUsername());
				actionJSON.setPassword(httpActionImpl.getPassword());
				actionJSON.setParameters(httpActionImpl.getParameters());
				break;
			case ACTION_VIDEO:
				VideoActionImpl videoActionImpl = (VideoActionImpl) action;

				actionJSON.setCameraSet(videoActionImpl.getCameraSet());
				break;
			case ACTION_REMOTE_VIDEO:
				RemoteVideoActionImpl remoteVideoActionImpl = (RemoteVideoActionImpl) action;
				actionJSON.setFreakName(remoteVideoActionImpl.getFreakName());
				actionJSON.setCameraSet(remoteVideoActionImpl.getCameraSet());
				break;
			case ACTION_CANCEL_ACTION:
				CancelActionActionImpl cancelActionActionImpl = (CancelActionActionImpl) action;
				actionJSON
						.setActionName(cancelActionActionImpl.getActionName());
				break;
			case ACTION_WEB_PREFIX:
				WebPrefixActionImpl webPrefixActionImpl = (WebPrefixActionImpl) action;
				actionJSON.setPrefix(webPrefixActionImpl.getPrefix());
				break;
			case ACTION_PHIDGET_OUTPUT:
				PhidgetActionImpl phidgetActionImpl = (PhidgetActionImpl) action;
				actionJSON.setPhidgetName(phidgetActionImpl.getPhidgetName());
				actionJSON.setPhidgetActionType(phidgetActionImpl
						.getPhidgetActionType());
				actionJSON.setPort(phidgetActionImpl.getPort());
				actionJSON.setPulseTrain(phidgetActionImpl.getPulseTrain());
				break;
			case ACTION_RFXCOM:
				RfxcomActionImpl rfxcomActionImpl = (RfxcomActionImpl) action;
				actionJSON.setRfxcomCommand(rfxcomActionImpl.getRfxcomName());
				break;
			}

			actionsJSON.getActions().add(actionJSON);
		}

		for (RemoteFreakSpec remoteFreakSpec : remoteFreakSpecList) {
			CameraListJSON cameraListJSON = null;
			try {
				cameraListJSON = remoteFreakSpec.getQueue().take();
				actionsJSON.getAvailableRemoteCameras().put(
						remoteFreakSpec.getFreakDevice().getName(),
						cameraListJSON.getCameraList());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		return actionsJSON;
	}

	private void setDefaults(Set<String> currentSet) {
		currentSet.add("actionType");
		currentSet.add("name");
		currentSet.add("eventName");
		currentSet.add("description");
		currentSet.add("guest");
		currentSet.add("validTimes");
		currentSet.add("delay");
		currentSet.add("delayUnits");
		currentSet.add("hysteresis");
		currentSet.add("hysteresisUnits");
		currentSet.add("profiles");
		currentSet.add("positiveTagNames");
		currentSet.add("negativeTagNames");
	}

	private void setupActionsJSON(ActionsJSON actionsJSON) {
		Set<String> currentSet;

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(ActionType.ACTION_ADD_TAG.toString(),
				currentSet);
		currentSet.add("validFor");
		currentSet.add("tagName");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(
				ActionType.ACTION_DELETE_TAG.toString(), currentSet);
		currentSet.add("tagName");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(ActionType.ACTION_EMAIL.toString(),
				currentSet);
		currentSet.add("to");
		currentSet.add("videoType");
		currentSet.add("cameraSet");
		currentSet.add("responseGroup");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(ActionType.ACTION_SEND_HTTP.toString(),
				currentSet);
		currentSet.add("protocol");
		currentSet.add("url");
		currentSet.add("methodType");
		currentSet.add("username");
		currentSet.add("password");
		currentSet.add("parameters");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(ActionType.ACTION_VIDEO.toString(),
				currentSet);
		currentSet.add("cameraSet");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(
				ActionType.ACTION_REMOTE_VIDEO.toString(), currentSet);
		currentSet.add("freakName");
		currentSet.add("cameraSet");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(
				ActionType.ACTION_CANCEL_ACTION.toString(), currentSet);
		currentSet.add("actionName");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(
				ActionType.ACTION_WEB_PREFIX.toString(), currentSet);
		currentSet.add("prefix");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(
				ActionType.ACTION_PHIDGET_OUTPUT.toString(), currentSet);
		currentSet.add("phidgetName");
		currentSet.add("phidgetActionType");
		currentSet.add("port");
		currentSet.add("pulseTrain");

		currentSet = new TreeSet<String>();
		setDefaults(currentSet);
		actionsJSON.getFieldUsage().put(ActionType.ACTION_RFXCOM.toString(),
				currentSet);
		currentSet.add("rfxcomCommand");
	}

	@RequestMapping("/actionsgetjson")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		if (freak == null)
			freak = Freak.getInstance();

		if (logger.isDebugEnabled())
			logger.debug("In service");

		sbtsConfig = freak.getSbtsConfig();

		if (logger.isDebugEnabled())
			logger.debug("Got config");
		if (logger.isDebugEnabled())
			logger.debug("eventNames: " + sbtsConfig.getAvailableEventNames());

		// Gson gson = new
		// GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("Got a gson object");

		PrintWriter out = response.getWriter();

		ActionsJSON actionsJSON = getActionsJSON();
		if (logger.isDebugEnabled())
			logger.debug("Got ActionsJSON");

		if (logger.isDebugEnabled())
			logger.debug("Output actionsJSON: " + gson.toJson(actionsJSON));
		out.print(gson.toJson(actionsJSON));
	}

}
