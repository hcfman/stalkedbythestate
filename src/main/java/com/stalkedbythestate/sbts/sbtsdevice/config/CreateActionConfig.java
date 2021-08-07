package com.stalkedbythestate.sbts.sbtsdevice.config;

import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

public class CreateActionConfig {
	private static final Logger logger = Logger
			.getLogger(CreateActionConfig.class);

	private static Element createCameraList(Document doc,
                                            SortedSet<Integer> cameraList) {
		Element cameraListElement = doc.createElement("cameraList");

		for (Integer cam : cameraList) {
			cameraListElement.appendChild(doc.createTextNode("\n\t\t\t\t"));
			Element camera = doc.createElement("camera");
			camera.setAttribute("index", Integer.toString(cam));
			cameraListElement.appendChild(camera);
		}
		cameraListElement.appendChild(doc.createTextNode("\n\t\t"));
		return cameraListElement;
	}

	private static Element createCameraListFromStringSet(Document doc,
                                                         Set<Integer> cameraList) {
		Element cameraListElement = doc.createElement("cameraList");

		for (Integer cam : cameraList) {
			cameraListElement.appendChild(doc.createTextNode("\n\t\t\t\t"));
			Element camera = doc.createElement("camera");
			camera.setAttribute("index", cam.toString());
			cameraListElement.appendChild(camera);
		}
		cameraListElement.appendChild(doc.createTextNode("\n\t\t"));
		return cameraListElement;
	}

	public static void addActionConfig(Document doc, Element parent,
                                       Collection<Action> actions) {
		for (Action action : actions) {
			int count = 0;
			Element actionElement = doc.createElement("action");
			parent.appendChild(doc.createTextNode("\n\n\t"));
			parent.appendChild(actionElement);
			actionElement.setAttribute("name", action.getName());
			actionElement.setAttribute("eventName", action.getEventName());
			actionElement.setAttribute("description", action.getDescription());
			if (logger.isDebugEnabled())
				logger.debug("actionType: " + action.getActionType());
			actionElement.setAttribute("type", action.getActionType()
					.toString());
			actionElement.setAttribute("delayFor",
					Integer.toString(action.getDelayFor()));
			actionElement.setAttribute("delayUnits", action.getDelayUnits()
					.toString());
			actionElement.setAttribute("hysteresisUnits", action
					.getHysteresisUnits().toString());
			actionElement.setAttribute("hysteresis",
					Integer.toString(action.getHysteresis()));
			actionElement.setAttribute("guest",
					Boolean.toString(action.isGuest()));

			EventCounter eventCounter = action.getEventcounter();
			if (eventCounter != null) {
				Element eventCounterElement = doc.createElement("counter");
				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				actionElement.appendChild(eventCounterElement);
				eventCounterElement.setAttribute("withinSeconds",
						Integer.toString(eventCounter.getWithinSeconds()));
				eventCounterElement.setAttribute("count",
						Integer.toString(eventCounter.getCount()));
			}

			if (action.getProfiles() != null && action.getProfiles().size() > 0) {
				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element profilesElement = doc.createElement("profiles");
				actionElement.appendChild(profilesElement);

				for (String profile : action.getProfiles()) {
					profilesElement.appendChild(doc.createTextNode("\n\t\t\t"));
					Element profileElement = doc.createElement("profile");
					profilesElement.appendChild(profileElement);
					profileElement.appendChild(doc.createTextNode(profile));
				}
				profilesElement.appendChild(doc.createTextNode("\n\t\t"));
				actionElement.appendChild(doc.createTextNode("\n"));
			}

			SortedSet<String> positiveTagNames = action.getPositiveTagNames();
			if (positiveTagNames != null && positiveTagNames.size() > 0) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element profilesElement = doc.createElement("positiveTagNames");
				if (action.isPositiveTagAndMode())
					profilesElement.setAttribute("andMode", "true");
				else
					profilesElement.setAttribute("andMode", "false");
					
				actionElement.appendChild(profilesElement);
				for (String tagName : positiveTagNames) {
					profilesElement.appendChild(doc.createTextNode("\n\t\t\t"));
					Element profileElement = doc.createElement("tagName");
					profilesElement.appendChild(profileElement);
					profileElement.appendChild(doc.createTextNode(tagName));
				}
				profilesElement.appendChild(doc.createTextNode("\n\t\t"));
			}

			SortedSet<String> negativeTagNames = action.getNegativeTagNames();
			if (negativeTagNames != null && negativeTagNames.size() > 0) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element profilesElement = doc.createElement("negativeTagNames");
				actionElement.appendChild(profilesElement);
				for (String tagName : negativeTagNames) {
					profilesElement.appendChild(doc.createTextNode("\n\t\t\t"));
					Element profileElement = doc.createElement("tagName");
					profilesElement.appendChild(profileElement);
					profileElement.appendChild(doc.createTextNode(tagName));
				}
				profilesElement.appendChild(doc.createTextNode("\n\t\t"));
			}

			if (action instanceof EmailActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element emailElement = doc.createElement("email");
				actionElement.appendChild(emailElement);

				EmailActionImpl emailAction = (EmailActionImpl) action;
				emailElement.setAttribute("to", emailAction.getTo());
				emailElement.setAttribute("responseGroup",
						emailAction.getResponseGroup());
				VideoType videoType = emailAction.getVideoType();
				if (videoType == null)
					videoType = VideoType.MJPEG;
				emailElement.setAttribute("videoType", videoType.toString());

				if (emailAction.getCameraSet().size() > 0) {
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
					actionElement.appendChild(createCameraListFromStringSet(
							doc, emailAction.getCameraSet()));
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
				}

			}

			if (action instanceof RemoteVideoActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element freakElement = doc.createElement("freak");
				actionElement.appendChild(freakElement);

				RemoteVideoActionImpl remoteVideoActionImpl = (RemoteVideoActionImpl) action;
				freakElement.appendChild(doc
						.createTextNode(remoteVideoActionImpl.getFreakName()));
			}

			if (action instanceof HttpActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				HttpActionImpl httpTriggerImpl = (HttpActionImpl) action;

				Element urlElement = doc.createElement("url");
				actionElement.appendChild(urlElement);
				urlElement.appendChild(doc.createTextNode(httpTriggerImpl
						.getUrl()));

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element typeElement = doc.createElement("requestType");
				actionElement.appendChild(typeElement);
				typeElement.appendChild(doc.createTextNode(httpTriggerImpl
						.getMethodType().toString()));

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element verifyHostnameElement = doc
						.createElement("verifyHostname");
				actionElement.appendChild(verifyHostnameElement);
				verifyHostnameElement.appendChild(doc.createTextNode(Boolean
						.toString(httpTriggerImpl.isVerifyHostname())));

				String username;
				if ((username = httpTriggerImpl.getUsername()) != null) {
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
					Element usernameElement = doc.createElement("username");
					actionElement.appendChild(usernameElement);
					usernameElement.appendChild(doc.createTextNode(username));

				}

				String password;
				if ((password = httpTriggerImpl.getPassword()) != null) {
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
					Element passwordElement = doc.createElement("password");
					actionElement.appendChild(passwordElement);
					passwordElement.appendChild(doc.createTextNode(password));

				}

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element parametersElement = doc.createElement("parameters");
				actionElement.appendChild(parametersElement);
				Map<String, String> paramMap = httpTriggerImpl.getParameters();
				if (paramMap.size() > 0) {
					String[] a = paramMap.keySet().toArray(new String[0])
							.clone();
					Arrays.sort(a);
					for (String key : a) {
						parametersElement.appendChild(doc
								.createTextNode("\n\t\t\t"));
						Element paramElement = doc.createElement("param");
						parametersElement.appendChild(paramElement);
						paramElement.setAttribute("name", key);
						paramElement.setAttribute("value", paramMap.get(key));

					}
					parametersElement.appendChild(doc.createTextNode("\n\t\t"));
				}

				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			if (action instanceof TagActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element tagElement = doc.createElement("tag");
				TagActionImpl tagActionImpl = (TagActionImpl) action;
				String tagName = tagActionImpl.getTagName();
				tagElement.setAttribute("name", tagName);
				actionElement.appendChild(tagElement);

				Long validFor = tagActionImpl.getValidFor();
				if (validFor != null) {
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
					Element validForElement = doc.createElement("validFor");
					actionElement.appendChild(validForElement);
					validForElement.setAttribute("period", validFor.toString());
				}
				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			if (action instanceof CancelActionActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element tagElement = doc.createElement("actionName");
				CancelActionActionImpl cancelActionActionImpl = (CancelActionActionImpl) action;
				String tagName = cancelActionActionImpl.getActionName();
				tagElement.appendChild(doc.createTextNode(tagName));
				actionElement.appendChild(tagElement);

				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			if (action instanceof VideoActionImpl) {
				VideoActionImpl cameraAction = (VideoActionImpl) action;
				if (!cameraAction.getCameraSet().isEmpty()) {
					actionElement.appendChild(doc.createTextNode("\n\n\t\t"));
					actionElement.appendChild(createCameraList(doc,
							cameraAction.getCameraSet()));
					actionElement.appendChild(doc.createTextNode("\n\t"));
				}
			}

			if (action instanceof WebPrefixActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				WebPrefixActionImpl webPrefixActionImpl = (WebPrefixActionImpl) action;
				actionElement.appendChild(doc.createTextNode("\n\t\t"));
				Element webPrefixElement = doc.createElement("webPrefix");
				actionElement.appendChild(webPrefixElement);
				webPrefixElement.appendChild(doc
						.createTextNode(webPrefixActionImpl.getPrefix()));

				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			if (action instanceof PhidgetActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				PhidgetActionImpl phidgetActionImpl = (PhidgetActionImpl) action;
				actionElement.appendChild(doc.createTextNode("\n\t\t"));

				Element phidgetActionType = doc.createElement("type");
				actionElement.appendChild(phidgetActionType);
				phidgetActionType.appendChild(doc
						.createTextNode(phidgetActionImpl
								.getPhidgetActionType().toString()));
				actionElement.appendChild(doc.createTextNode("\n\t\t"));

				Element phidgetNameElement = doc.createElement("phidgetName");
				actionElement.appendChild(phidgetNameElement);
				phidgetNameElement.appendChild(doc
						.createTextNode(phidgetActionImpl.getPhidgetName()));
				actionElement.appendChild(doc.createTextNode("\n\t\t"));

				Element portElement = doc.createElement("port");
				actionElement.appendChild(portElement);
				portElement.appendChild(doc.createTextNode(Integer
						.toString(phidgetActionImpl.getPort())));

				if (phidgetActionImpl.getPhidgetActionType() == PhidgetActionType.Pulse) {
					actionElement.appendChild(doc.createTextNode("\n\t\t"));
					Element phidgetPulseTrainElement = doc
							.createElement("pulseTrain");
					actionElement.appendChild(phidgetPulseTrainElement);
					phidgetPulseTrainElement.appendChild(doc
							.createTextNode(phidgetActionImpl.getPulseTrain()));
				}

				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			if (action instanceof RfxcomActionImpl) {
				if (count > 0)
					actionElement.appendChild(doc.createTextNode("\n"));
				count++;

				RfxcomActionImpl rfxcomActionImpl = (RfxcomActionImpl) action;
				actionElement.appendChild(doc.createTextNode("\n\t\t"));

				Element rfxcomCommandElement = doc.createElement("command");
				actionElement.appendChild(rfxcomCommandElement);
				rfxcomCommandElement.appendChild(doc
						.createTextNode(rfxcomActionImpl.getRfxcomName()));
				actionElement.appendChild(doc.createTextNode("\n\t"));
			}

			Collection<TimeSpec> validTimes = action.getValidTimes();
			if (validTimes != null && validTimes.size() > 0) {
				Element validTimesElement = TimeSpecListToXML.toXML(doc,
						validTimes, 2);
				actionElement.appendChild(doc.createTextNode("\n"
						+ StringHelper.repeatChar('\t', 2)));
				actionElement.appendChild(validTimesElement);
				actionElement.appendChild(doc.createTextNode("\n\t"));
			}
		}
	}
}
