package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomOperator;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomType;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.*;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class SbtsDeviceConfig {
	private static final Logger logger = LoggerFactory.getLogger(SbtsDeviceConfig.class);
	private final String DEFAULT_VERSION = "1.0";
	private String version = DEFAULT_VERSION;
	private volatile ProfileConfig profileConfig = new ProfileConfig();
	private volatile EmailConfig emailConfig = new EmailConfig();
	private volatile CameraConfig cameraConfig = new CameraConfig();
	private volatile HttpConfig httpConfig = new HttpConfig();
	private volatile TimerConfig timerConfig = new TimerConfig();
	private volatile ActionConfig actionConfig = new ActionConfig();
	private volatile SynthTriggerConfig synthTriggerConfig = new SynthTriggerConfig();
	private volatile WatchdogConfig watchdogConfig = new WatchdogConfig();
	private volatile SettingsConfig settingsConfig = new SettingsConfig();
	private volatile ScheduleConfig scheduleConfig = new ScheduleConfig();
	private volatile FreakConfig freakConfig = new FreakConfig();
	private volatile PhidgetConfig phidgetConfig = new PhidgetConfig();
	private volatile CertificateConfig certificateConfig = new CertificateConfig();
	private volatile LinksConfig linksConfig = new LinksConfig();
	private volatile DiskConfig diskConfig = new DiskConfig();
	private volatile RfxcomConfig rfxcomConfig = new RfxcomConfig();
	private volatile DownloadConfig downloadConfig = new DownloadConfig();

	public SbtsDeviceConfig() {
	}

	public SbtsDeviceConfig(String filename) throws Exception {
		loadXML(filename);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		if (version == null)
			version = DEFAULT_VERSION;
		else
			this.version = version;
	}

	public CameraConfig getCameraConfig() {
		return cameraConfig;
	}

	public void setCameraConfig(CameraConfig cameraConfig) {
		this.cameraConfig = cameraConfig;
	}

	public TimerConfig getTimerConfig() {
		return timerConfig;
	}

	public void setTimerConfig(TimerConfig timerConfig) {
		this.timerConfig = timerConfig;
	}

	public ProfileConfig getProfileConfig() {
		return profileConfig;
	}

	public void setProfileConfig(ProfileConfig profileConfig) {
		this.profileConfig = profileConfig;
	}

	public EmailConfig getEmailConfig() {
		return emailConfig;
	}

	public void setEmailConfig(EmailConfig emailConfig) {
		this.emailConfig = emailConfig;
	}

	public ActionConfig getActionConfig() {
		return actionConfig;
	}

	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	public SynthTriggerConfig getSynthTriggerConfig() {
		return synthTriggerConfig;
	}

	public WatchdogConfig getWatchdogConfig() {
		return watchdogConfig;
	}

	public void setSynthTriggerConfig(SynthTriggerConfig synthTriggerConfig) {
		this.synthTriggerConfig = synthTriggerConfig;
	}

	public SettingsConfig getSettingsConfig() {
		return settingsConfig;
	}

	public void setSettingsConfig(SettingsConfig settingsConfig) {
		this.settingsConfig = settingsConfig;
	}

	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	public ScheduleConfig getScheduleConfig() {
		return scheduleConfig;
	}

	public void setSchedulesConfig(ScheduleConfig scheduleConfig) {
		this.scheduleConfig = scheduleConfig;
	}

	public List<String> getAvailableEventNames() {
		Set<String> eventSet = new TreeSet<String>();

		for (String groupName : httpConfig.getGroupMap().keySet())
			for (HttpTrigger httpTrigger : httpConfig.getGroupMap().get(groupName))
				eventSet.add(httpTrigger.getEventName());

		if (synthTriggerConfig != null)
			for (SynthTrigger synthTrigger : synthTriggerConfig.getTriggerMap().values()) {
				eventSet.add(synthTrigger.getResult());
			}

		if (watchdogConfig != null)
			for (Watchdog watchdogTrigger : watchdogConfig.getTriggerMap().values()) {
				eventSet.add(watchdogTrigger.getResult());
			}

		if (phidgetConfig != null && phidgetConfig.getPhidgetMap() != null)
			for (PhidgetDevice phidgetDevice : phidgetConfig.getPhidgetMap().values()) {
				for (int i = 0; i < PhidgetConstants.PHIDGET_PORT_SIZE; i++) {
					if (phidgetDevice.getOffTriggerEventNames()[i] != null
							&& !phidgetDevice.getOffTriggerEventNames()[i].trim().equals(""))
						eventSet.add(phidgetDevice.getOffTriggerEventNames()[i]);
					if (phidgetDevice.getOnTriggerEventNames()[i] != null
							&& !phidgetDevice.getOnTriggerEventNames()[i].trim().equals(""))
						eventSet.add(phidgetDevice.getOnTriggerEventNames()[i]);
				}
			}

		if (scheduleConfig != null)
			for (Schedule schedule : scheduleConfig.getSchedules())
				eventSet.add(schedule.getEventName());

		if (rfxcomConfig != null && rfxcomConfig.getCommands() != null)
			for (RfxcomCommand command : rfxcomConfig.getCommands().values())
				if (command.getRfxcomType() == RfxcomType.GENERIC_INPUT)
					eventSet.add(command.getEventName());

		return new ArrayList<String>(eventSet);
	}

	public Collection<String> getAvailableRfxcomEventNames() {
		Collection<String> eventSet = new TreeSet<String>();

		if (rfxcomConfig != null && rfxcomConfig.getCommands() != null)
			for (RfxcomCommand rfxcomCommand : rfxcomConfig.getCommands().values()) {
				String eventName = rfxcomCommand.getEventName();
				if (eventName == null || eventName.equals(""))
					continue;

				eventSet.add(eventName);
			}

		return eventSet;
	}

	public Collection<String> getAvailableButtonEventNames() {
		Collection<String> eventSet = new TreeSet<String>();

		if (httpConfig != null && httpConfig.getGroupMap().size() > 0) {
			Map<String, List<HttpTrigger>> groupMap = httpConfig.getGroupMap();
			for (String groupName : httpConfig.getGroupMap().keySet()) {
				List<HttpTrigger> httpTriggerList = groupMap.get(groupName);
				for (HttpTrigger httpTrigger : httpTriggerList) {
					String eventName = httpTrigger.getEventName();
					if (eventName == null || eventName.equals(""))
						continue;
					eventSet.add(eventName);
				}

			}
		}

		return eventSet;
	}

	public Collection<String> getAvailablePhidgetEventnames() {
		Collection<String> eventSet = new TreeSet<String>();

		if (phidgetConfig != null && phidgetConfig.getPhidgetMap() != null)
			for (PhidgetDevice phidgetDevice : phidgetConfig.getPhidgetMap().values()) {
				for (int i = 0; i < PhidgetConstants.PHIDGET_PORT_SIZE; i++) {
					if (phidgetDevice.getOffTriggerEventNames()[i] != null
							&& !phidgetDevice.getOffTriggerEventNames()[i].trim().equals(""))
						eventSet.add(phidgetDevice.getOffTriggerEventNames()[i]);
					if (phidgetDevice.getOnTriggerEventNames()[i] != null
							&& !phidgetDevice.getOnTriggerEventNames()[i].trim().equals(""))
						eventSet.add(phidgetDevice.getOnTriggerEventNames()[i]);
				}
			}

		return eventSet;
	}

	public List<String> getAvailableProfiles() {
		Set<String> profileTagNameSet = new TreeSet<String>();

		if (profileConfig != null && profileConfig.getProfileList() != null)
			for (Profile profile : profileConfig.getProfileList())
				profileTagNameSet.add(profile.getTagName());

		return new ArrayList<String>(profileTagNameSet);
	}

	public List<String> getAvailableTagNames() {
		Set<String> tagNameSet = new TreeSet<String>();

		if (profileConfig != null && profileConfig.getProfileList() != null)
			for (Profile profile : profileConfig.getProfileList())
				tagNameSet.add(profile.getTagName());

		if (actionConfig != null && actionConfig.getActionList() != null)
			for (Action action : actionConfig.getActionList()) {
				ActionType actionType = action.getActionType();
				if (actionType == ActionType.ACTION_ADD_TAG || actionType == ActionType.ACTION_DELETE_TAG) {
					TagActionImpl tagActionImpl = (TagActionImpl) action;
					tagNameSet.add(tagActionImpl.getTagName());
				}
			}

		return new ArrayList<String>(tagNameSet);
	}

	public List<String> getAvailableFreakNames() {
		Set<String> freakNameSet = new TreeSet<String>();

		if (freakConfig != null && freakConfig.getFreakMap() != null)
			for (FreakDevice freakDevice : freakConfig.getFreakMap().values())
				freakNameSet.add(freakDevice.getName());

		return new ArrayList<String>(freakNameSet);
	}

	public List<String> getAvailableRfxcomCommands() {
		Set<String> rfxcommandSet = new TreeSet<String>();

		if (rfxcomConfig != null && rfxcomConfig.getCommands() != null)
			for (RfxcomCommand rfxcomCommand : rfxcomConfig.getCommands().values())
				rfxcommandSet.add(rfxcomCommand.getName());

		return new ArrayList<String>(rfxcommandSet);
	}

	public List<String> getAvailableButtonGroups() {
		Set<String> buttonNameSet = new TreeSet<String>();

		if (httpConfig != null && httpConfig.getGroupsAsList() != null)
			for (String buttonGroup : httpConfig.getGroupsAsList())
				buttonNameSet.add(buttonGroup);

		return new ArrayList<String>(buttonNameSet);
	}

	public List<String> getAvailableCameras() {
		Set<Integer> cameraSet = new TreeSet<Integer>();
		List<String> cameraList = new ArrayList<String>();

		if (cameraConfig != null && cameraConfig.getCameraDevices() != null)
			for (int cameraDevice : cameraConfig.getCameraDevices().keySet())
				cameraSet.add(cameraDevice);

		// Return in sorted order
		for (int i : cameraSet) {
			cameraList.add(Integer.toString(i));
		}

		return cameraList;
	}

	public List<String> getAvailablePhidgets() {
		List<String> phidgetList = new ArrayList<String>();
		// Map<String, PhidgetDevice> getPhidgetMap()

		if (phidgetConfig != null && !phidgetConfig.getPhidgetMap().keySet().isEmpty())
			for (String name : phidgetConfig.getPhidgetMap().keySet())
				phidgetList.add(name);

		return phidgetList;
	}

	public FreakConfig getFreakConfig() {
		return freakConfig;
	}

	public void setFreakConfig(FreakConfig freakConfig) {
		this.freakConfig = freakConfig;
	}

	public PhidgetConfig getPhidgetConfig() {
		return phidgetConfig;
	}

	public void setPhidgetConfig(PhidgetConfig phidgetConfig) {
		this.phidgetConfig = phidgetConfig;
	}

	public void setScheduleConfig(ScheduleConfig scheduleConfig) {
		this.scheduleConfig = scheduleConfig;
	}

	public CertificateConfig getCertificateConfig() {
		return certificateConfig;
	}

	public void setCertificateConfig(CertificateConfig certificateConfig) {
		this.certificateConfig = certificateConfig;
	}

	public LinksConfig getLinksConfig() {
		return linksConfig;
	}

	public void setLinksConfig(LinksConfig linksConfig) {
		this.linksConfig = linksConfig;
	}

	public DiskConfig getDiskConfig() {
		return diskConfig;
	}

	public void setDiskConfig(DiskConfig diskConfig) {
		this.diskConfig = diskConfig;
	}

	public DownloadConfig getDownloadConfig() {
		return downloadConfig;
	}

	public void setDownloadConfig(DownloadConfig downloadConfig) {
		this.downloadConfig = downloadConfig;
	}

	public RfxcomConfig getRfxcomConfig() {
		return rfxcomConfig;
	}

	public void setRfxcomConfig(RfxcomConfig rfxcomConfig) {
		this.rfxcomConfig = rfxcomConfig;
	}

	private void loadSettings(XPath xpath, InputSource source) throws XPathExpressionException {
		// Fetch the version first
		NodeList settingsNodes = (NodeList) xpath.evaluate("/sbtsConfig/settings", source, XPathConstants.NODESET);

		if (settingsNodes.getLength() > 0) {
			Element settingsElement = (Element) settingsNodes.item(0);

			NodeList webPrefixNodes = settingsElement.getElementsByTagName("webPrefix");
			if (webPrefixNodes.getLength() > 0) {
				Element webPrefixElement = (Element) webPrefixNodes.item(0);
				String webPrefixString = webPrefixElement.getTextContent();
				settingsConfig.setWebPrefix(webPrefixString);
			}

			NodeList encoderThreadsNodes = settingsElement.getElementsByTagName("encoderThreads");
			if (encoderThreadsNodes.getLength() > 0) {
				Element encoderThreadsElement = (Element) encoderThreadsNodes.item(0);
				String encoderThreadsString = encoderThreadsElement.getTextContent();
				if (encoderThreadsString.length() > 0 && encoderThreadsString.matches("^\\d+$")) {
					try {
						settingsConfig.setEncoderThreads(Integer.parseInt(encoderThreadsString));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList emailThreadsNodes = settingsElement.getElementsByTagName("emailThreads");
			if (emailThreadsNodes.getLength() > 0) {
				Element emailThreadsElement = (Element) emailThreadsNodes.item(0);
				String emailThreadsString = emailThreadsElement.getTextContent();
				if (emailThreadsString.length() > 0 && emailThreadsString.matches("^\\d+$")) {
					try {
						settingsConfig.setEmailThreads((Integer.parseInt(emailThreadsString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList httpThreadsNodes = settingsElement.getElementsByTagName("httpThreads");
			if (httpThreadsNodes.getLength() > 0) {
				Element httpThreadsElement = (Element) httpThreadsNodes.item(0);
				String httpThreadsString = httpThreadsElement.getTextContent();
				if (httpThreadsString.length() > 0 && httpThreadsString.matches("^\\d+$")) {
					try {
						settingsConfig.setHttpThreads((Integer.parseInt(httpThreadsString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList connectTimeoutNodes = settingsElement.getElementsByTagName("connectTimeout");
			if (connectTimeoutNodes.getLength() > 0) {
				Element connectTimeoutElement = (Element) connectTimeoutNodes.item(0);
				String connectTimeoutString = connectTimeoutElement.getTextContent();
				if (connectTimeoutString.length() > 0 && connectTimeoutString.matches("^\\d+$")) {
					try {
						settingsConfig.setConnectTimeout((Integer.parseInt(connectTimeoutString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList checkMountNodes = settingsElement.getElementsByTagName("checkMount");
			if (checkMountNodes.getLength() > 0) {
				Element checkMountElement = (Element) checkMountNodes.item(0);
				String checkMountString = checkMountElement.getTextContent();
				if (checkMountString.length() > 0) {
					try {
						settingsConfig.setCheckMount(Boolean.parseBoolean((checkMountString)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			NodeList freeSpaceNodes = settingsElement.getElementsByTagName("freeSpace");
			if (freeSpaceNodes.getLength() > 0) {
				Element freeSpaceElement = (Element) freeSpaceNodes.item(0);
				String freeSpaceString = freeSpaceElement.getTextContent();
				if (freeSpaceString.length() > 0 && freeSpaceString.matches("^\\d+$")) {
					try {
						settingsConfig.setFreeSpace((Integer.parseInt(freeSpaceString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList daysMJPGNodes = settingsElement.getElementsByTagName("daysMJPG");
			if (daysMJPGNodes.getLength() > 0) {
				Element daysMJPGElement = (Element) daysMJPGNodes.item(0);
				String daysMJPGString = daysMJPGElement.getTextContent();
				if (daysMJPGString.length() > 0 && daysMJPGString.matches("^\\d+$")) {
					try {
						settingsConfig.setDaysMJPG((Integer.parseInt(daysMJPGString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList cleanRateNodes = settingsElement.getElementsByTagName("cleanRate");
			if (cleanRateNodes.getLength() > 0) {
				Element cleanRateElement = (Element) cleanRateNodes.item(0);
				String cleanRateString = cleanRateElement.getTextContent();
				if (cleanRateString.length() > 0 && cleanRateString.matches("^\\d+$")) {
					try {
						settingsConfig.setCleanRate((Integer.parseInt(cleanRateString)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			NodeList phoneHomeNodes = settingsElement.getElementsByTagName("phoneHome");
			if (phoneHomeNodes.getLength() > 0) {
				if (logger.isDebugEnabled())
					logger.debug("There is a phoneHome node");
				Element phoneHomeElement = (Element) phoneHomeNodes.item(0);
				String phoneHomeString = phoneHomeElement.getTextContent();
				settingsConfig.setPhoneHome(phoneHomeString.trim());
			}

			NodeList keystorePasswordNodes = settingsElement.getElementsByTagName("keystorePassword");
			if (keystorePasswordNodes.getLength() > 0) {
				if (logger.isDebugEnabled())
					logger.debug("There is a keystorePassword node");
				Element keystorePasswordElement = (Element) keystorePasswordNodes.item(0);
				String keystorePasswordString = keystorePasswordElement.getTextContent();
				settingsConfig.setKeystorePassword(keystorePasswordString.trim());
			}

			NodeList truststorePasswordNodes = settingsElement.getElementsByTagName("truststorePassword");
			if (truststorePasswordNodes.getLength() > 0) {
				if (logger.isDebugEnabled())
					logger.debug("There is a truststorePassword node");
				Element truststorePasswordElement = (Element) truststorePasswordNodes.item(0);
				String truststorePasswordString = truststorePasswordElement.getTextContent();
				settingsConfig.setTruststorePassword(truststorePasswordString.trim());
			}

			if (settingsElement.getElementsByTagName("forceUpdate").getLength() > 0)
				settingsConfig.setForceUpdate(true);
		}
	}

	private void loadProfiles(XPath xpath, InputSource source) throws XPathExpressionException {
		// Fetch the version first
		NodeList versionNodes = (NodeList) xpath.evaluate("/sbtsConfig/version", source, XPathConstants.NODESET);

		if (versionNodes.getLength() > 0) {
			Element versionElement = (Element) versionNodes.item(0);
			String versionString = versionElement.getTextContent();
			if (versionString == null)
				version = DEFAULT_VERSION;
			else
				version = versionString;
		}

		NodeList profileNodes = (NodeList) xpath.evaluate("/sbtsConfig/profiles/profile", source,
				XPathConstants.NODESET);

		setProfileConfig(new ProfileConfig());
		for (int i = 0; i < profileNodes.getLength(); i++) {
			// Create ProfileImpl
			Element profileElement = (Element) profileNodes.item(i);
			String name = profileElement.getAttribute("name");
			String tagName = profileElement.getAttribute("tagName");
			String description = profileElement.getAttribute("description");

			if (tagName == null || tagName.matches("^\\s*$"))
				continue;

			Profile profile = new ProfileImpl(name, tagName, description);

			// Add isOn boolean
			NodeList isOnNodes = profileElement.getElementsByTagName("isOn");
			if (isOnNodes.getLength() > 0) {
				Element isOnElement = (Element) isOnNodes.item(0);
				Boolean isOn = Boolean.parseBoolean(isOnElement.getAttribute("value"));
				if (isOn != null)
					profile.setIsOn(isOn);
			}

			try {
				profile.setValidTimes(TimeSpec.parseTimeSpecs(xpath, profileElement));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Add profile if all went well
			getProfileConfig().add(profile);
		}
	}

	private void loadLinks(XPath xpath, InputSource source) throws XPathExpressionException {
		if (logger.isDebugEnabled())
			logger.debug("Loading links");
		NodeList linksNodes = (NodeList) xpath.evaluate("/sbtsConfig/links/link", source, XPathConstants.NODESET);

		for (int i = 0; i < linksNodes.getLength(); i++) {
			// Create Link
			Element linkElement = (Element) linksNodes.item(i);
			String name = linkElement.getAttribute("name");
			String linkString = linkElement.getTextContent();

			if (name == null || name.matches("^\\s*$"))
				continue;

			if (linkString == null || linkString.matches("^\\s*$"))
				continue;

			if (logger.isDebugEnabled())
				logger.debug("Load link: " + name + " " + linkString);

			Link link = new Link(name, linkString);
			getLinksConfig().getLinkList().add(link);
		}
	}

	private void loadEmailProvider(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList emailNodes = (NodeList) xpath.evaluate("/sbtsConfig/email", source, XPathConstants.NODESET);

		if (emailNodes.getLength() <= 0)
			return;

		Element emailElement = (Element) emailNodes.item(0);

		String name = emailElement.getAttribute("name");
		String description = emailElement.getAttribute("description");

		if (name == null || name.trim().equals(""))
			return;

		String mailhost = null;
		NodeList mailhostNodes = emailElement.getElementsByTagName("mailhost");
		if (mailhostNodes.getLength() > 0) {
			mailhost = mailhostNodes.item(0).getTextContent();
		}

		String from = null;
		NodeList fromNodes = emailElement.getElementsByTagName("from");
		if (fromNodes.getLength() > 0) {
			from = fromNodes.item(0).getTextContent();
		}

		String username = null;
		NodeList usernameNodes = emailElement.getElementsByTagName("username");
		if (usernameNodes.getLength() > 0) {
			username = usernameNodes.item(0).getTextContent();
		}

		String password = null;
		NodeList passwordNodes = emailElement.getElementsByTagName("password");
		if (passwordNodes.getLength() > 0) {
			password = passwordNodes.item(0).getTextContent();
		}

		String portString = null;
		NodeList portNodes = emailElement.getElementsByTagName("port");
		if (portNodes.getLength() > 0) {
			portString = portNodes.item(0).getTextContent();
		}

		String encTypeString = null;
		NodeList encTypeNodes = emailElement.getElementsByTagName("encType");
		if (encTypeNodes.getLength() > 0) {
			encTypeString = encTypeNodes.item(0).getTextContent();
		} else {
			encTypeString = "ENC_PLAIN";
		}

		EmailProviderImpl emailProviderImpl = new EmailProviderImpl(name, description, mailhost, from);
		if (username != null && !username.trim().equals(""))
			emailProviderImpl.setUsername(username);
		if (password != null && !password.trim().equals(""))
			emailProviderImpl.setPassword(password);
		emailProviderImpl.setPort(portString == null || portString.equals("") ? 25 : Integer.parseInt(portString));
		emailProviderImpl.setEncryptionType(EncryptionType.set(encTypeString));
		emailConfig = new EmailConfig();
		emailConfig.setEmailProvider(emailProviderImpl);
	}

	private void loadHttpTriggers(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList groupNodes = (NodeList) xpath.evaluate("/sbtsConfig/http/group", source, XPathConstants.NODESET);

		if (groupNodes.getLength() <= 0)
			return;

		httpConfig = new HttpConfig();

		for (int i = 0; i < groupNodes.getLength(); i++) {
			Element groupElement = (Element) groupNodes.item(i);

			String groupName = groupElement.getAttribute("name");
			if (groupName == null || groupName.trim().equals(""))
				continue;

			if (logger.isDebugEnabled())
				logger.debug("Loaded group: " + groupName);
			NodeList httpTriggerNodes = groupElement.getElementsByTagName("httpTrigger");

			if (httpTriggerNodes.getLength() <= 0)
				return;

			List<HttpTrigger> httpTriggers = new ArrayList<HttpTrigger>();
			for (int j = 0; j < httpTriggerNodes.getLength(); j++) {

				Element httpTriggerElement = (Element) httpTriggerNodes.item(j);
				String eventName = httpTriggerElement.getAttribute("eventName");
				String description = httpTriggerElement.getAttribute("description");

				boolean isGuest = false;
				String guest = httpTriggerElement.getAttribute("guest");
				if (guest != null)
					isGuest = Boolean.parseBoolean(guest);

				// Skip if no eventName
				if (eventName == null || eventName.trim().equals(""))
					continue;

				if (description == null)
					description = "";

				httpTriggers.add(new HttpTrigger(eventName, description, isGuest));
			}

			if (logger.isDebugEnabled())
				logger.debug("Putting(" + groupName + ") = " + httpTriggers);
			httpConfig.getGroupMap().put(groupName, httpTriggers);
		}

		if (logger.isDebugEnabled())
			logger.debug("Returning, keySet: " + httpConfig.getGroupMap().keySet().toString());

	}

	private void loadSchedules(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList scheduleNodes = (NodeList) xpath.evaluate("/sbtsConfig/schedules/schedule", source,
				XPathConstants.NODESET);

		if (scheduleNodes.getLength() <= 0)
			return;

		List<Schedule> schedules = new ArrayList<Schedule>();

		for (int i = 0; i < scheduleNodes.getLength(); i++) {
			Element scheduleElement = (Element) scheduleNodes.item(i);
			String name = scheduleElement.getAttribute("name");

			// Skip if no name
			if (name == null || name.trim().equals(""))
				continue;

			NodeList eventNameNodes = scheduleElement.getElementsByTagName("eventName");
			if (eventNameNodes.getLength() <= 0)
				continue;
			String eventName = eventNameNodes.item(0).getTextContent();
			// Skip if no eventName
			if (eventName == null || eventName.trim().equals(""))
				continue;

			List<TimeSpec> timeSpecs;
			try {
				timeSpecs = TimeSpec.parseTimeSpecs(xpath, scheduleElement);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			if (timeSpecs.isEmpty())
				continue;

			TimeSpec timeSpec = timeSpecs.get(0);

			schedules.add(new Schedule(name, timeSpec, eventName));

		}
		scheduleConfig.setSchedules(schedules);
	}

	private void loadCameras(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList cameraNodes = (NodeList) xpath.evaluate("/sbtsConfig/cameras/camera", source, XPathConstants.NODESET);

		cameraConfig = new CameraConfig();
		Map<Integer, CameraDevice> cameraList = cameraConfig.getCameraDevices();
		if (cameraNodes.getLength() <= 0)
			return;

		for (int i = 0; i < cameraNodes.getLength(); i++) {
			Element cameraElement = (Element) cameraNodes.item(i);
			String name = cameraElement.getAttribute("name");
			int index = Integer.parseInt(cameraElement.getAttribute("index"));
			boolean enabled = Boolean.parseBoolean(cameraElement.getAttribute("enabled"));
			String description = cameraElement.getAttribute("description");
			String url = cameraElement.getAttribute("url");
			String username = cameraElement.getAttribute("username");
			String password = cameraElement.getAttribute("password");
			String framesPerSecond = cameraElement.getAttribute("framesPerSecond");
			String bufferSeconds = cameraElement.getAttribute("bufferSeconds");
			String priority = cameraElement.getAttribute("priority");
			String continueSeconds = cameraElement.getAttribute("continueSeconds");
			Boolean cachingAllowed = Boolean.parseBoolean(cameraElement.getAttribute("cachingAllowed"));
			Boolean guest = Boolean.parseBoolean(cameraElement.getAttribute("guest"));
			CameraDevice cameraDevice = new CameraDeviceImpl(name, index, enabled, description, url, username, password,
					Integer.parseInt(continueSeconds), Integer.parseInt(bufferSeconds),
					Integer.parseInt(framesPerSecond), Integer.parseInt(priority));
			if (cachingAllowed != null)
				cameraDevice.setCachingAllowed(cachingAllowed);
			if (guest != null)
				cameraDevice.setGuest(guest);
			cameraList.put(index, cameraDevice);
		}
		cameraConfig.setCameraDevices(cameraList);
	}

	private void loadLinkedFreaks(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList freakNodes = (NodeList) xpath.evaluate("/sbtsConfig/linkedFreaks/freak", source,
				XPathConstants.NODESET);

		if (freakNodes.getLength() <= 0)
			return;

		Map<String, FreakDevice> freakMap = freakConfig.getFreakMap();

		for (int i = 0; i < freakNodes.getLength(); i++) {
			Element freakElement = (Element) freakNodes.item(i);
			String name = freakElement.getAttribute("name");
			;
			String description = freakElement.getAttribute("description");

			String hostname = freakElement.getAttribute("hostname");
			int port = 80;
			String portString = freakElement.getAttribute("port");
			if (portString != null)
				port = Integer.parseInt(portString);

			String protocol = freakElement.getAttribute("protocol");
			if (protocol == null || protocol.equals(""))
				protocol = "HTTP";

			String guestString = freakElement.getAttribute("guest");
			boolean guest = false;
			try {
				guest = Boolean.parseBoolean(guestString);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String verifyHostnameString = freakElement.getAttribute("verifyHostname");
			boolean verifyHostname = false;
			try {
				verifyHostname = Boolean.parseBoolean(verifyHostnameString);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String username = freakElement.getAttribute("username");
			String password = freakElement.getAttribute("password");
			freakMap.put(name, new FreakDeviceImpl(name, description, hostname, port, username, password,
					ProtocolType.set(protocol), verifyHostname, guest));
		}
	}

	private void loadPhidgets(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList phidgetNodes = (NodeList) xpath.evaluate("/sbtsConfig/phidgets/phidget", source,
				XPathConstants.NODESET);

		if (phidgetNodes.getLength() <= 0)
			return;

		Map<String, PhidgetDevice> phidgetMap = phidgetConfig.getPhidgetMap();

		for (int i = 0; i < phidgetNodes.getLength(); i++) {
			Element phidgetElement = (Element) phidgetNodes.item(i);
			String name = phidgetElement.getAttribute("name");
			;
			String description = phidgetElement.getAttribute("description");
			int serialNumber = Integer.parseInt(phidgetElement.getAttribute("serialNumber"));

			String portSizeString;
			int portSize = 8;
			if ((portSizeString = phidgetElement.getAttribute("portSize")) != null && !"".equals(portSizeString))
				portSize = Integer.parseInt(portSizeString);

			PhidgetDeviceImpl phidgetDevice = new PhidgetDeviceImpl(name, description, serialNumber, portSize);

			boolean[] initialInputState = phidgetDevice.getInitialInputState();
			String[] onTriggerEventNames = phidgetDevice.getOnTriggerEventNames();
			String[] offTriggerEventNames = phidgetDevice.getOffTriggerEventNames();
			NodeList inputPortNodes = phidgetElement.getElementsByTagName("inputPorts");
			if (inputPortNodes.getLength() > 0) {
				Element inputPortsElement = (Element) inputPortNodes.item(0);
				NodeList portElementNodes = inputPortsElement.getElementsByTagName("port");
				for (int j = 0; j < portElementNodes.getLength(); j++) {
					if (logger.isDebugEnabled())
						logger.debug("Got an input port");
					Element portElement = (Element) portElementNodes.item(j);
					String indexString = portElement.getAttribute("index");
					if (logger.isDebugEnabled())
						logger.debug("Index string: " + indexString);
					if (indexString == null)
						continue;

					int index;
					try {
						index = Integer.parseInt(indexString);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}

					String valueString = portElement.getTextContent();
					if (valueString == null)
						continue;

					boolean value;
					try {
						value = Boolean.parseBoolean(valueString);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					String onTriggerEventName = portElement.getAttribute("onTriggerEventName");
					if (logger.isDebugEnabled())
						logger.debug("onTriggerEventName (" + j + "): \"" + onTriggerEventName + "\"");
					if (onTriggerEventName != null && !onTriggerEventName.equals(""))
						onTriggerEventNames[j] = onTriggerEventName;

					String offTriggerEventName = portElement.getAttribute("offTriggerEventName");
					if (logger.isDebugEnabled())
						logger.debug("offTriggerEventName (" + j + "): \"" + offTriggerEventName + "\"");
					if (offTriggerEventName != null && !offTriggerEventName.equals(""))
						offTriggerEventNames[j] = offTriggerEventName;

					if (logger.isDebugEnabled())
						logger.debug("Setting initialInputState[" + index + "] to " + value);
					initialInputState[index] = value;
				}
			}

			boolean[] initialOutputState = phidgetDevice.getInitialOutputState();
			NodeList outputPortNodes = phidgetElement.getElementsByTagName("outputPorts");
			if (outputPortNodes.getLength() > 0) {
				Element inputPortsElement = (Element) outputPortNodes.item(0);
				NodeList portElementNodes = inputPortsElement.getElementsByTagName("port");
				for (int j = 0; j < portElementNodes.getLength(); j++) {
					if (logger.isDebugEnabled())
						logger.debug("Got an input port");
					Element portElement = (Element) portElementNodes.item(j);
					String indexString = portElement.getAttribute("index");
					if (logger.isDebugEnabled())
						logger.debug("Index string: " + indexString);
					if (indexString == null)
						continue;

					int index;
					try {
						index = Integer.parseInt(indexString);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}

					String valueString = portElement.getTextContent();
					if (valueString == null)
						continue;

					boolean value;
					try {
						value = Boolean.parseBoolean(valueString);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					if (logger.isDebugEnabled())
						logger.debug("Setting initialOutputState[" + index + "] to " + value);
					initialOutputState[index] = value;
				}
			}

			phidgetMap.put(name, phidgetDevice);
		}
	}

	private void loadRfxcom(XPath xpath, InputSource source) throws XPathExpressionException {
		NodeList rfxcomCommandNodes = (NodeList) xpath.evaluate("/sbtsConfig/rfxcom/command", source,
				XPathConstants.NODESET);

		if (rfxcomCommandNodes.getLength() <= 0)
			return;

		Map<String, RfxcomCommand> rfxcomCommandMap = new HashMap<String, RfxcomCommand>();

		for (int i = 0; i < rfxcomCommandNodes.getLength(); i++) {
			Element commandElement = (Element) rfxcomCommandNodes.item(i);
			String name = commandElement.getAttribute("name");

			String description = commandElement.getAttribute("description");
			String rfxcomTypeString = commandElement.getAttribute("type");
			if (rfxcomTypeString == "")
				continue;
			RfxcomCommand rfxcomCommand = new RfxcomCommand(name, description);

			RfxcomType rfxcomType = RfxcomType.set(rfxcomTypeString);
			if (rfxcomType == null)
				continue;

			rfxcomCommand = new RfxcomCommand(name, description, rfxcomType);

			// values1
			NodeList values1NodeList = commandElement.getElementsByTagName("values1");
			String values1String;
			if (values1NodeList.getLength() > 0)
				values1String = values1NodeList.item(0).getTextContent();
			else
				values1String = "";
			String[] values1StringArray = values1String.split(",");
			int[] values1 = new int[values1StringArray.length];

			int count = 0;
			for (String s : values1StringArray) {
				try {
					values1[count] = Integer.parseInt(s, 16);
				} catch (NumberFormatException e) {
					logger.error("Can't parse integer value (" + s + ")", e);
				}
				count++;
			}
			rfxcomCommand.setPacketValues1(values1);

			switch (rfxcomType) {
			case GENERIC_INPUT:
				NodeList eventNameNodes = commandElement.getElementsByTagName("eventName");
				if (eventNameNodes.getLength() > 0)
					rfxcomCommand.setEventName(eventNameNodes.item(0).getTextContent());
				else
					rfxcomCommand.setEventName("");

				NodeList hysteresisNodes = commandElement.getElementsByTagName("hysteresis");
				if (hysteresisNodes.getLength() > 0) {
					String hysteresisString = hysteresisNodes.item(0).getTextContent();
					int hysteresis = 0;
					try {
						hysteresis = Integer.parseInt(hysteresisString);
					} catch (NumberFormatException e) {
						logger.error("Can't parse hysteresis value (" + hysteresisString + ")", e);
					}
					rfxcomCommand.setHysteresis(hysteresis);
				} else
					rfxcomCommand.setHysteresis(0);

				// values2
				NodeList values2NodeList = commandElement.getElementsByTagName("values2");
				String values2String;
				if (values2NodeList.getLength() > 0)
					values2String = values2NodeList.item(0).getTextContent();
				else
					values2String = "";
				String[] values2StringArray = values2String.split(",");
				int[] values2 = new int[values2StringArray.length];

				count = 0;
				for (String s : values2StringArray) {
					try {
						values2[count] = Integer.parseInt(s, 16);
					} catch (NumberFormatException e) {
						logger.error("Can't parse integer value (" + s + ")", e);
					}
					count++;
				}
				rfxcomCommand.setPacketValues2(values2);

				// mask
				NodeList maskNodeList = commandElement.getElementsByTagName("mask");
				String maskString;
				if (maskNodeList.getLength() > 0)
					maskString = maskNodeList.item(0).getTextContent();
				else
					maskString = "";
				String[] maskStringArray = maskString.split(",");
				int[] mask = new int[maskStringArray.length];

				count = 0;
				for (String s : maskStringArray) {
					try {
						mask[count] = Integer.parseInt(s, 16);
					} catch (NumberFormatException e) {
						logger.error("Can't parse integer value (" + s + ")", e);
					}
					count++;
				}
				rfxcomCommand.setMask(mask);

				// operator
				NodeList operatorNodeList = commandElement.getElementsByTagName("operator");
				String operatorString;
				if (operatorNodeList.getLength() > 0)
					operatorString = operatorNodeList.item(0).getTextContent();
				else
					operatorString = "";
				String[] operatorStringArray = operatorString.split(",");
				RfxcomOperator[] operator = new RfxcomOperator[operatorStringArray.length];

				count = 0;
				for (String s : operatorStringArray) {
					operator[count] = RfxcomOperator.set(s);
					count++;
				}

				rfxcomCommand.setOperator(operator);

				break;
			case GENERIC_OUTPUT:
				break;
			}

			rfxcomCommandMap.put(rfxcomCommand.getName(), rfxcomCommand);
		}

		rfxcomConfig.setCommands(rfxcomCommandMap);
	}

	private void addProfiles(Action action, XPath xpath, Element node) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("In addProfiles");

		if (profileConfig == null)
			return;

		if (logger.isDebugEnabled())
			logger.debug("There is a profileConfig");

		/*
		 * Add tagNames from required profiles
		 */

		if (logger.isDebugEnabled())
			logger.debug("Adding positive profiles");
		NodeList profileNodes = node.getElementsByTagName("profile");
		if (profileNodes != null) {
			SortedSet<String> profileSet = action.getProfiles();
			for (int i = 0; i < profileNodes.getLength(); i++) {
				Element profileElement = (Element) profileNodes.item(i);
				String tagName = profileElement.getTextContent();
				profileSet.add(tagName);
				if (logger.isDebugEnabled())
					logger.debug("Adding profile: " + tagName);
			}
		}

		/*
		 * Add positive acting tagnames
		 */
		if (logger.isDebugEnabled())
			logger.debug("Adding positive profiles");
		NodeList positiveTagNamesNodes = node.getElementsByTagName("positiveTagNames");
		if (positiveTagNamesNodes != null)
			for (int i = 0; i < positiveTagNamesNodes.getLength(); i++) {
				Element positiveProfileElement = (Element) positiveTagNamesNodes.item(i);
				NodeList positiveProfileNodes = positiveProfileElement.getElementsByTagName("tagName");

				if (positiveProfileNodes.getLength() > 0) {
					String andModeString = positiveProfileElement.getAttribute("andMode");
					if (andModeString.length() > 0) {
						boolean andMode = false;
						try {
							andMode = Boolean.parseBoolean(andModeString);
						} catch (Exception e) {
							logger.error("Can't parse andMode attribute as a Boolean");
						}
						action.setPositiveTagAndMode(andMode);
					}

					SortedSet<String> positiveTagNames = action.getPositiveTagNames();

					for (int j = 0; j < positiveProfileNodes.getLength(); j++) {
						Element profileElement1 = (Element) positiveProfileNodes.item(j);
						String tagName = profileElement1.getTextContent();
						positiveTagNames.add(tagName);
					}

					action.setPositiveTagNames(positiveTagNames);
				}
			}

		/*
		 * Add negative active tagNames
		 */
		if (logger.isDebugEnabled())
			logger.debug("Adding negative profiles");
		NodeList negativeTagNamesNodes = node.getElementsByTagName("negativeTagNames");
		if (negativeTagNamesNodes != null)
			for (int i = 0; i < negativeTagNamesNodes.getLength(); i++) {
				Element negativeProfileElement = (Element) negativeTagNamesNodes.item(i);
				NodeList negativeProfileNodes = negativeProfileElement.getElementsByTagName("tagName");

				if (negativeProfileNodes.getLength() > 0) {
					SortedSet<String> negativeTagNames = action.getNegativeTagNames();

					for (int j = 0; j < negativeProfileNodes.getLength(); j++) {
						Element profileElement2 = (Element) negativeProfileNodes.item(j);
						String tagName = profileElement2.getTextContent();
						negativeTagNames.add(tagName);
					}

					action.setNegativeTagNames(negativeTagNames);
				}
			}

	}

	private void addCameras(Action trigger, XPath xpath, Element node) throws Exception {

		if (cameraConfig == null)
			return;

		if (!(trigger instanceof HasCameras)) {
			return;
		}

		NodeList cameraNodes = node.getElementsByTagName("camera");

		if (cameraNodes.getLength() > 0) {
			SortedSet<Integer> cameraList = ((HasCameras) trigger).getCameraSet();

			for (int i = 0; i < cameraNodes.getLength(); i++) {
				Element cameraElement = (Element) cameraNodes.item(i);
				String index = cameraElement.getAttribute("index");
				cameraList.add(Integer.parseInt(index));
			}

		}

	}

	private void loadActions(XPath xpath, Document doc) throws Exception {
		NodeList actionNodes = (NodeList) xpath.evaluate("/sbtsConfig/actions/action", doc, XPathConstants.NODESET);

		if (actionNodes.getLength() <= 0)
			return;

		NodeList cameraNodeList;
		Set<Integer> cameraIndexSet;
		String videoTypeString = null;
		VideoType videoType = null;

		actionConfig = new ActionConfig();
		for (int i = 0; i < actionNodes.getLength(); i++) {
			Element actionElement = (Element) actionNodes.item(i);
			String typeString = actionElement.getAttribute("type");
			if (typeString == null)
				continue;

			ActionType actionType;
			// Skip if not known. Allows for deprecating actions
			try {
				actionType = ActionType.set(typeString);
			} catch (ActionTypeException e1) {
				logger.warn("Unknown action type: " + e1.getMessage());
				continue;
			}

			// Get counter if it exists
			EventCounter eventCounter = null;
			NodeList counterNodes = actionElement.getElementsByTagName("counter");
			if (counterNodes.getLength() > 0) {
				Element counterElement = (Element) counterNodes.item(0);
				String countString = counterElement.getAttribute("count");
				String withinSecondsString = counterElement.getAttribute("withinSeconds");

				int count = 0;
				int withinSeconds = 0;
				try {
					count = Integer.parseInt(countString);
					withinSeconds = Integer.parseInt(withinSecondsString);

					eventCounter = new EventCounter(count, withinSeconds);
				} catch (Exception e) {
					logger.error("Can't parse counter attributes", e);
				}
			}

			boolean guest = false;
			String guestString = actionElement.getAttribute("guest");
			try {
				if (guestString != null && !guestString.trim().equals(""))
					guest = Boolean.parseBoolean(guestString);
			} catch (Exception e1) {
				logger.error("Exception parsing boolean from \"" + guestString + "\"");
				e1.printStackTrace();
			}

			Action action = null;

			String actionName = actionElement.getAttribute("name");
			String eventName = actionElement.getAttribute("eventName");
			String description = actionElement.getAttribute("description");

			int delayFor = 0;
			String delayForString;
			if ((delayForString = actionElement.getAttribute("delayFor")) != null
					&& !delayForString.trim().equals("")) {
				if (logger.isDebugEnabled())
					logger.debug("delayFor string \"" + delayForString + "\"");
				delayFor = Integer.parseInt(delayForString);
			}

			UnitType delayUnits = UnitType.sec;
			String delayUnitsString;
			if ((delayUnitsString = actionElement.getAttribute("delayUnits")) != null
					&& !delayUnitsString.trim().equals("")) {
				if (logger.isDebugEnabled())
					logger.debug("delayUnits string \"" + delayUnitsString + "\"");
				delayUnits = UnitType.set(delayUnitsString);
			}

			UnitType hysteresisUnits = UnitType.sec;
			String hysteresisUnitsString;
			if ((hysteresisUnitsString = actionElement.getAttribute("hysteresisUnits")) != null
					&& !hysteresisUnitsString.trim().equals("")) {
				if (logger.isDebugEnabled())
					logger.debug("hysteresisUnits string \"" + hysteresisUnitsString + "\"");
				hysteresisUnits = UnitType.set(hysteresisUnitsString);
			}

			int hysteresis = 0;
			String hysteresisString;
			if ((hysteresisString = actionElement.getAttribute("hysteresis")) != null
					&& !hysteresisString.trim().equals("")) {
				if (logger.isDebugEnabled())
					logger.debug("hysteresis string \"" + hysteresisString + "\"");
				hysteresis = Integer.parseInt(hysteresisString);
			}

			switch (actionType) {
			case ACTION_EMAIL:
				NodeList emailNodes = actionElement.getElementsByTagName("email");

				if (emailNodes.getLength() <= 0)
					continue;

				Element emailElement = (Element) emailNodes.item(0);
				String address = emailElement.getAttribute("to");
				videoTypeString = emailElement.getAttribute("videoType");
				videoType = VideoType.MJPEG;
				if (logger.isDebugEnabled())
					logger.debug("videoTypeString: " + videoTypeString);
				if (videoTypeString != null && !videoTypeString.equals(""))
					videoType = VideoType.set(videoTypeString);
				String responseGroup = emailElement.getAttribute("responseGroup");
				EmailActionImpl emailActionImpl = new EmailActionImpl(actionName, eventName, description, address);
				emailActionImpl.setVideoType(videoType);
				emailActionImpl.setResponseGroup(responseGroup);

				action = emailActionImpl;

				cameraNodeList = actionElement.getElementsByTagName("camera");
				if (cameraNodeList.getLength() <= 0) {
					if (logger.isDebugEnabled())
						logger.debug("There are none: " + cameraNodeList);
					break;
				}

				cameraIndexSet = emailActionImpl.getCameraSet();
				for (int j = 0; j < cameraNodeList.getLength(); j++) {
					Element cameraIndexElement = (Element) cameraNodeList.item(j);
					String cameraIndexString = cameraIndexElement.getAttribute("index");

					if (cameraIndexString == null)
						continue;

					try {
						int cameraIndex = Integer.parseInt(cameraIndexString);
						cameraIndexSet.add(cameraIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				break;
			case ACTION_SEND_HTTP:
				if (logger.isDebugEnabled())
					logger.debug("Load ACTION_SEND_HTTP");
				NodeList urlNodes = actionElement.getElementsByTagName("url");
				if (urlNodes.getLength() <= 0) {
					if (logger.isDebugEnabled())
						logger.debug("No url nodes, skipping");
					continue;
				}
				String url = urlNodes.item(0).getTextContent();
				if (logger.isDebugEnabled())
					logger.debug("url = " + url);

				String methodTypeString = null;
				NodeList typeNodes = actionElement.getElementsByTagName("requestType");
				if (typeNodes.getLength() <= 0) {
					if (logger.isDebugEnabled())
						logger.debug("No type node, skipping");
					methodTypeString = "GET";
				} else
					methodTypeString = typeNodes.item(0).getTextContent();

				if (logger.isDebugEnabled())
					logger.debug("Check verifyHostnameString");
				boolean verifyHostname = false;
				NodeList verifyHostnameNodes = actionElement.getElementsByTagName("verifyHostname");
				if (verifyHostnameNodes.getLength() > 0) {
					String verifyHostnameString = verifyHostnameNodes.item(0).getTextContent();
					if (logger.isDebugEnabled())
						logger.debug("verifyHostnameString: " + verifyHostnameString);
					try {
						verifyHostname = Boolean.parseBoolean(verifyHostnameString);
					} catch (Exception e) {
						logger.error("Can't parse verifyHostname field");
					}
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Can't find verifyHostnameString");
				}

				NodeList usernameNodes = actionElement.getElementsByTagName("username");
				if (usernameNodes.getLength() <= 0)
					continue;
				String username = usernameNodes.item(0).getTextContent();

				NodeList passwordNodes = actionElement.getElementsByTagName("password");
				if (passwordNodes.getLength() <= 0)
					continue;
				String password = passwordNodes.item(0).getTextContent();

				HttpActionImpl httpTriggerImpl = new HttpActionImpl(actionName, eventName, description, url,
						MethodType.set(methodTypeString), verifyHostname, username, password);

				NodeList parametersNodes = actionElement.getElementsByTagName("parameters");

				if (parametersNodes.getLength() > 0) {
					Element parametersElement = (Element) parametersNodes.item(0);
					NodeList paramNodes = parametersElement.getElementsByTagName("param");

					for (int j = 0; j < paramNodes.getLength(); j++) {
						Element paramElement = (Element) paramNodes.item(j);
						String name = paramElement.getAttribute("name");
						if (name == null)
							continue;

						String value = paramElement.getAttribute("value");
						if (value == null)
							continue;

						httpTriggerImpl.put(name, value);
					}
				}

				action = httpTriggerImpl;

				break;
			case ACTION_ADD_TAG:
			case ACTION_DELETE_TAG:
				NodeList tagNodes = actionElement.getElementsByTagName("tag");

				if (tagNodes.getLength() <= 0)
					continue;

				Element tagElement = (Element) tagNodes.item(0);
				String tagName = tagElement.getAttribute("name");

				TagActionType tagActionType;
				if (actionType == ActionType.ACTION_ADD_TAG)
					tagActionType = TagActionType.SET_ON;
				else
					tagActionType = TagActionType.SET_OFF;

				Long validFor = null;
				NodeList validForNodes = actionElement.getElementsByTagName("validFor");
				if (validForNodes.getLength() > 0) {
					Element validForElement = (Element) validForNodes.item(0);
					String periodString = validForElement.getAttribute("period");
					if (periodString != null) {
						try {
							validFor = Long.parseLong(periodString);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				TagActionImpl tagActionImpl = new TagActionImpl(actionName, eventName, description, tagActionType,
						tagName);
				tagActionImpl.setValidFor(validFor);
				tagActionImpl.setTagName(tagName);

				action = tagActionImpl;
				break;
			case ACTION_VIDEO:
				if (cameraConfig == null)
					continue;

				NodeList cameraNodes = actionElement.getElementsByTagName("camera");

				if (cameraNodes.getLength() <= 0)
					continue;

				SortedSet<Integer> cameraSet = new TreeSet<Integer>();
				for (int k = 0; k < cameraNodes.getLength(); k++) {
					Element cameraElement = (Element) cameraNodes.item(k);
					String indexString = cameraElement.getAttribute("index");
					if (indexString == null)
						continue;

					int camera = 0;
					try {
						camera = Integer.parseInt(indexString);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					CameraDevice cameraDevice = cameraConfig.get(camera);
					if (cameraDevice == null)
						continue;
					cameraSet.add(camera);
				}

				if (cameraSet.size() == 0)
					continue;

				VideoActionImpl cameraTriggerImpl = new VideoActionImpl(actionName, eventName, description, cameraSet);

				action = cameraTriggerImpl;
				break;
			case ACTION_REMOTE_VIDEO:
				NodeList freakNodes = actionElement.getElementsByTagName("freak");

				if (freakNodes.getLength() <= 0) {
					continue;
				}

				Element freakElement = (Element) freakNodes.item(0);
				String freakName = freakElement.getTextContent();
				if (freakName == null || freakName.trim().equals("")) {
					continue;
				}

				NodeList remoteCameraNodes = actionElement.getElementsByTagName("camera");

				if (remoteCameraNodes.getLength() <= 0) {
					continue;
				}

				SortedSet<Integer> remoteCameraSet = new TreeSet<Integer>();
				for (int k = 0; k < remoteCameraNodes.getLength(); k++) {
					Element cameraElement = (Element) remoteCameraNodes.item(k);
					String indexString = cameraElement.getAttribute("index");
					if (indexString == null)
						continue;

					int camera = 0;
					try {
						camera = Integer.parseInt(indexString);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					remoteCameraSet.add(camera);
				}

				if (remoteCameraSet.size() == 0)
					continue;

				RemoteVideoActionImpl remoteCameraTriggerImpl = new RemoteVideoActionImpl(actionName, eventName,
						description, remoteCameraSet, freakName);

				action = remoteCameraTriggerImpl;
				break;
			case ACTION_CANCEL_ACTION:
				if (logger.isDebugEnabled())
					logger.debug("Parsing cancel action action");
				NodeList actionNameNodes = actionElement.getElementsByTagName("actionName");

				if (actionNameNodes.getLength() <= 0)
					continue;

				Element actionNameElement = (Element) actionNameNodes.item(0);

				String cancelActionActionName = actionNameElement.getTextContent();

				CancelActionActionImpl cancelActionActionImpl = new CancelActionActionImpl(actionName, eventName,
						description, cancelActionActionName);

				action = cancelActionActionImpl;
				break;
			case ACTION_WEB_PREFIX:
				if (logger.isDebugEnabled())
					logger.debug("Parsing webPrefix action");
				NodeList webPrefixNodes = actionElement.getElementsByTagName("webPrefix");

				if (webPrefixNodes.getLength() <= 0)
					continue;

				Element webPrefixElement = (Element) webPrefixNodes.item(0);

				String prefix = webPrefixElement.getTextContent();

				WebPrefixActionImpl webPrefixActionImpl = new WebPrefixActionImpl(actionName, eventName, description,
						prefix);

				action = webPrefixActionImpl;
				break;
			case ACTION_PHIDGET_OUTPUT:
				if (logger.isDebugEnabled())
					logger.debug("Parsing ACTION_PHIDGET_OUTPUT");
				NodeList phidgetTypeNodes = actionElement.getElementsByTagName("type");

				if (phidgetTypeNodes.getLength() <= 0)
					continue;

				Element phidgetTypeElement = (Element) phidgetTypeNodes.item(0);
				String phidgetActionTypeString = phidgetTypeElement.getTextContent();

				if (phidgetActionTypeString == null || phidgetActionTypeString.equals(""))
					continue;

				PhidgetActionType phidgetActionType = PhidgetActionType.set(phidgetActionTypeString);

				NodeList phidgetNameNodes = actionElement.getElementsByTagName("phidgetName");

				if (phidgetNameNodes.getLength() <= 0)
					continue;

				Element phidgetNameElement = (Element) phidgetNameNodes.item(0);
				String phidgetName = phidgetNameElement.getTextContent();

				if (phidgetName == null || phidgetName.equals(""))
					continue;

				NodeList portNodes = actionElement.getElementsByTagName("port");

				if (portNodes.getLength() <= 0)
					continue;

				Element portElement = (Element) portNodes.item(0);
				String portString = portElement.getTextContent();

				if (portString == null || portString.equals(""))
					continue;

				int port = 0;
				try {
					port = Integer.parseInt(portString);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				String pulseTrain = null;
				if (phidgetActionType == PhidgetActionType.Pulse) {
					NodeList pulseNodes = actionElement.getElementsByTagName("pulseTrain");

					if (pulseNodes.getLength() <= 0)
						continue;

					Element pulseElement = (Element) pulseNodes.item(0);
					pulseTrain = pulseElement.getTextContent();

					if (pulseTrain == null || pulseTrain.equals(""))
						continue;
				}

				PhidgetActionImpl phidgetActionImpl;
				if (phidgetActionType == PhidgetActionType.Pulse)
					phidgetActionImpl = new PhidgetActionImpl(actionName, eventName, description, phidgetName, port,
							phidgetActionType, pulseTrain);
				else
					phidgetActionImpl = new PhidgetActionImpl(actionName, eventName, description, phidgetName, port,
							phidgetActionType);

				action = phidgetActionImpl;
				break;
			case ACTION_RFXCOM:
				if (logger.isDebugEnabled())
					logger.debug("Parsing ACTION_RFXCOM");
				NodeList rfxcomCommandNodes = actionElement.getElementsByTagName("command");

				if (rfxcomCommandNodes.getLength() <= 0)
					continue;

				String rfxcomCommand = rfxcomCommandNodes.item(0).getTextContent();

				RfxcomActionImpl rfxcomActionImpl = new RfxcomActionImpl(actionName, eventName, description,
						rfxcomCommand);
				action = rfxcomActionImpl;
				break;
			}

			if (logger.isDebugEnabled())
				logger.debug("Broken out");

			// If any parsing fails, skip reading this element in
			if (action == null) {
				if (logger.isDebugEnabled())
					logger.debug("No action selected, skipping");
				continue;
			}
			if (logger.isDebugEnabled())
				logger.debug("Now add profiles");

			addProfiles(action, xpath, actionElement);
			addCameras(action, xpath, actionElement);
			action.setValidTimes(TimeSpec.parseTimeSpecs(xpath, actionElement));
			action.setDelayFor(delayFor);
			action.setDelayUnits(delayUnits);
			action.setHysteresis(hysteresis);
			action.setHysteresisUnits(hysteresisUnits);
			action.setGuest(guest);
			action.setEventcounter(eventCounter);

			if (actionType == ActionType.ACTION_EMAIL
					|| actionType == ActionType.ACTION_SEND_HTTP || actionType == ActionType.ACTION_ADD_TAG
					|| actionType == ActionType.ACTION_DELETE_TAG || actionType == ActionType.ACTION_VIDEO
					|| actionType == ActionType.ACTION_REMOTE_VIDEO || actionType == ActionType.ACTION_CANCEL_ACTION
					|| actionType == ActionType.ACTION_WEB_PREFIX || actionType == ActionType.ACTION_PHIDGET_OUTPUT
					|| actionType == ActionType.ACTION_RFXCOM) {
				actionConfig.add(action);
			}
		}
	}

	private void loadSynthTriggers(XPath xpath, Document doc) throws Exception {
		NodeList synthTriggerNodes = (NodeList) xpath.evaluate("/sbtsConfig/synthTriggers/synthTrigger", doc,
				XPathConstants.NODESET);

		if (synthTriggerNodes.getLength() <= 0)
			return;

		for (int i = 0; i < synthTriggerNodes.getLength(); i++) {
			Element synthTriggerElement = (Element) synthTriggerNodes.item(i);

			NodeList resultNodes = synthTriggerElement.getElementsByTagName("result");
			if (resultNodes.getLength() <= 0)
				continue;

			String result = ((Element) resultNodes.item(0)).getTextContent();
			if (result == null || result.trim().equals(""))
				continue;

			NodeList withinSecondsNodes = synthTriggerElement.getElementsByTagName("withinSeconds");
			if (withinSecondsNodes.getLength() <= 0)
				continue;

			String withinSeconds = ((Element) withinSecondsNodes.item(0)).getTextContent();
			if (withinSeconds == null || withinSeconds.trim().equals(""))
				continue;

			Collection<String> eventNames = new ArrayList<String>();

			NodeList eventNameList = synthTriggerElement.getElementsByTagName("eventName");
			for (int j = 0; j < eventNameList.getLength(); j++) {
				Element eventNameListElement = (Element) eventNameList.item(j);
				String eventName = eventNameListElement.getTextContent();

				if (eventName == null || eventName.trim().equals(""))
					continue;

				eventNames.add(eventName);
			}

			if (eventNames.size() <= 0)
				continue;

			SynthTriggerImpl synthTriggerImpl = new SynthTriggerImpl(result, Integer.parseInt(withinSeconds),
					eventNames);

			if (synthTriggerConfig == null)
				synthTriggerConfig = new SynthTriggerConfig();

			synthTriggerConfig.getTriggerMap().put(result, synthTriggerImpl);
		}
	}

	private void loadWatchdogs(XPath xpath, Document doc) throws Exception {
		NodeList watchdogNodes = (NodeList) xpath.evaluate("/sbtsConfig/watchdogs/watchdog", doc,
				XPathConstants.NODESET);

		if (watchdogNodes.getLength() <= 0)
			return;

		for (int i = 0; i < watchdogNodes.getLength(); i++) {
			Element watchdogElement = (Element) watchdogNodes.item(i);

			NodeList resultNodes = watchdogElement.getElementsByTagName("result");
			if (resultNodes.getLength() <= 0)
				continue;

			String result = ((Element) resultNodes.item(0)).getTextContent();
			if (result == null || result.trim().equals(""))
				continue;

			NodeList withinSecondsNodes = watchdogElement.getElementsByTagName("withinSeconds");
			if (withinSecondsNodes.getLength() <= 0)
				continue;

			String withinSeconds = ((Element) withinSecondsNodes.item(0)).getTextContent();
			if (withinSeconds == null || withinSeconds.trim().equals(""))
				continue;

			Collection<String> eventNames = new ArrayList<String>();

			NodeList eventNameList = watchdogElement.getElementsByTagName("eventName");
			for (int j = 0; j < eventNameList.getLength(); j++) {
				Element eventNameListElement = (Element) eventNameList.item(j);
				String eventName = eventNameListElement.getTextContent();

				if (eventName == null || eventName.trim().equals(""))
					continue;

				eventNames.add(eventName);
			}

			if (eventNames.size() <= 0)
				continue;

			WatchdogImpl watchdogImpl = new WatchdogImpl(result, Long.parseLong(withinSeconds), eventNames);

			if (watchdogConfig == null)
				watchdogConfig = new WatchdogConfig();

			watchdogConfig.getTriggerMap().put(result, watchdogImpl);
		}
	}

	public void loadXML(String filename) throws Exception {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this! (Apparently)
		Document doc = null;
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse(filename);

		XPath xpath = XPathFactory.newInstance().newXPath();
		InputSource source = new InputSource(filename);
		loadSettings(xpath, source);
		loadProfiles(xpath, source);
		loadLinks(xpath, source);
		loadEmailProvider(xpath, source);
		loadCameras(xpath, source);
		loadHttpTriggers(xpath, source);
		loadSchedules(xpath, source);
		loadLinkedFreaks(xpath, source);
		loadPhidgets(xpath, source);
		loadRfxcom(xpath, source);

		loadActions(xpath, doc);
		loadSynthTriggers(xpath, doc);
		loadWatchdogs(xpath, doc);
	}

	public void outputXML(String filename) throws IOException, TransformerException, ParserConfigurationException {
		DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = documentBuilder.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// create the root element
		Element root = doc.createElement("sbtsConfig");
		// all it to the xml tree
		doc.appendChild(root);

		root.appendChild(doc.createTextNode("\n\n"));

		if (version != null) {
			Element versionElement = doc.createElement("version");
			root.appendChild(versionElement);
			versionElement.appendChild(doc.createTextNode(version));
		}

		root.appendChild(doc.createTextNode("\n\n"));

		settingsConfig.addSettingsConfig(doc, root);
		root.appendChild(doc.createTextNode("\n\n"));

		if (profileConfig != null)
			profileConfig.addProfileConfig(doc, root);

		root.appendChild(doc.createTextNode("\n\n"));

		if (linksConfig != null)
			linksConfig.addLinksConfig(doc, root);

		if (emailConfig != null)
			emailConfig.addEmailConfig(doc, root);

		if (cameraConfig != null)
			cameraConfig.addCameraConfig(doc, root);

		if (httpConfig != null)
			httpConfig.addHttpConfig(doc, root);

		if (scheduleConfig != null)
			scheduleConfig.addScheduleConfig(doc, root);

		if (freakConfig != null)
			freakConfig.addFreakConfig(doc, root);

		if (phidgetConfig != null)
			phidgetConfig.addPhidgetConfig(doc, root);

		if (rfxcomConfig != null)
			rfxcomConfig.addRfxcomConfig(doc, root);

		if (actionConfig != null) {
			Element triggersElement = doc.createElement("actions");
			root.appendChild(triggersElement);
			CreateActionConfig.addActionConfig(doc, triggersElement, actionConfig.getActionList());
			triggersElement.appendChild(doc.createTextNode("\n\n"));
			root.appendChild(doc.createTextNode("\n\n"));

		}

		if (synthTriggerConfig != null) {
			Element synthTriggersElement = doc.createElement("synthTriggers");
			root.appendChild(synthTriggersElement);
			CreateSynthTriggerConfig.addTriggerConfig(doc, synthTriggersElement,
					synthTriggerConfig.getTriggerMap().values());
			root.appendChild(doc.createTextNode("\n\n"));

		}

		if (watchdogConfig != null) {
			Element watchdogElement = doc.createElement("watchdogs");
			root.appendChild(watchdogElement);
			CreateWatchdogConfig.addTriggerConfig(doc, watchdogElement,
					watchdogConfig.getTriggerMap().values());
			root.appendChild(doc.createTextNode("\n\n"));

		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		Source src = new DOMSource(doc);

		File file = new File(filename);

		FileOutputStream outStream = new FileOutputStream(file);

		Result dest = new StreamResult(outStream);
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.transform(src, dest);
		outStream.close();

	}

	@Override
	public String toString() {
		return "SbtsDeviceConfig [DEFAULT_VERSION=" + DEFAULT_VERSION + ", version=" + version + ", profileConfig="
				+ profileConfig + ", emailConfig=" + emailConfig + ", cameraConfig=" + cameraConfig
				+ ", httpConfig=" + httpConfig + ", timerConfig=" + timerConfig + ", actionConfig="
				+ actionConfig + ", synthTriggerConfig=" + synthTriggerConfig + ", settingsConfig=" + settingsConfig
				+ ", scheduleConfig=" + scheduleConfig + ", freakConfig=" + freakConfig + ", phidgetConfig="
				+ phidgetConfig + ", certificateConfig=" + certificateConfig + ", linksConfig=" + linksConfig
				+ ", diskConfig=" + diskConfig + ", rfxcomConfig=" + rfxcomConfig + "]";
	}

}
