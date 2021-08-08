package com.stalkedbythestate.sbts.freak;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.cleanerhandler.CleanerHandler;
import com.stalkedbythestate.sbts.diskwatchdog.DiskWatchdogHandler;
import com.stalkedbythestate.sbts.dvrhandler.DvrHandler;
import com.stalkedbythestate.sbts.eh.EventHandler;
import com.stalkedbythestate.sbts.email.EmailHandler;
import com.stalkedbythestate.sbts.eventlib.*;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.httphandler.HttpHandler;
import com.stalkedbythestate.sbts.phidgetqueuehandler.PhidgetQueueHandler;
import com.stalkedbythestate.sbts.rfxcomhandler.RfxcomHandler;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.sbtsdevice.config.SettingsConfig;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.HttpActionImpl;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.MethodType;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Freak implements FreakApi {
	private static final Logger logger = Logger.getLogger(Freak.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private static Freak freak;
	private final String SBTS_BASE = System.getenv("SBTS_HOME");
	private static volatile SbtsDeviceConfig sbtsConfig;
	private static AtomicBoolean updating = new AtomicBoolean();
	private static EventHandler eventHandler;
	private static EmailHandler emailHandler;
	private static DvrHandler dvrHandler;
	private static HttpHandler httpHandler;
	private static PhidgetQueueHandler phidgetQueueHandler;
	private static CleanerHandler cleanerHandler;
	private static DiskWatchdogHandler diskWatchdogHandler;
	private static RfxcomHandler rfxcomHandler;
	private static LinkedBlockingQueue<Event> ehEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> emailEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> dvrEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> smsEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> httpEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> cleanerEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> phidgetQueueHandlerEventQueue = new LinkedBlockingQueue<Event>();
	private static LinkedBlockingQueue<Event> rfxcomEventQueue = new LinkedBlockingQueue<Event>();
	private static Map<EventListener, Pattern> listenerMap = new ConcurrentHashMap<EventListener, Pattern>();
	private static boolean ready = false;

	private Freak() {
	}

	public void start() {
		sbtsConfig = getSbtsConfig();

		Properties properties = new Properties();

		String propFile = "sbts.properties";
		java.net.URL url = Thread.currentThread().getContextClassLoader()
				.getResource(propFile);
		try {
			InputStream is = url.openStream();
			if (is == null) {
				logger.error("Can't load sbts.properties");
			} else {
				properties.load(is);
				sbtsConfig.getSettingsConfig().setVersion(
						properties.getProperty("sbts.version"));
				opLogger.info("Copyright (C) Kim Hendrikse, 2021");
				opLogger.info("Version: "
						+ sbtsConfig.getSettingsConfig().getVersion());
			}
		} catch (Exception e) {
			logger.error("Cannot read sbts properties: " + e.getMessage());
			logger.error("Can't obtain resource as a stream");
		}

		// CM15a controller
		eventHandler = new EventHandler(freak, ehEventQueue);
		eventHandler.start();

		// E-mailer
		emailHandler = new EmailHandler(freak, emailEventQueue);
		emailHandler.start();

		dvrHandler = new DvrHandler(freak, dvrEventQueue);
		dvrHandler.start();

		httpHandler = new HttpHandler(freak, httpEventQueue);
		httpHandler.start();

		phidgetQueueHandler = new PhidgetQueueHandler(freak,
				phidgetQueueHandlerEventQueue);
		phidgetQueueHandler.start();

		cleanerHandler = new CleanerHandler(freak, cleanerEventQueue);
		cleanerHandler.start();

		rfxcomHandler = new RfxcomHandler(freak, rfxcomEventQueue);
		rfxcomHandler.start();

		if (sbtsConfig.getSettingsConfig().isCheckMount()) {
			diskWatchdogHandler = new DiskWatchdogHandler();
			diskWatchdogHandler.start(this);
		} else {
			sbtsConfig.getDiskConfig().setDiskState(DiskState.ALL_GOOD);
		}

		System.setProperty("javax.net.ssl.keyStore", SBTS_BASE + "/" + "cacerts"
				+ "/" + "keystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", sbtsConfig
				.getSettingsConfig().getKeystorePassword());
		System.setProperty("javax.net.ssl.trustStore", SBTS_BASE + "/" + "certs"
				+ "/" + "truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", sbtsConfig
				.getSettingsConfig().getTruststorePassword());
		// OurHostnameVerifier.setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());

		phoneHome();

		ready = true;
	}

	public void sendEvent(Event event) {
		ehEventQueue.add(event);
	}

	public void sendEmailEvent(Event event) {
		emailEventQueue.add(event);
	}

	public void sendPhidgetEvent(Event event) {
		phidgetQueueHandlerEventQueue.add(event);
	}

	public void sendDvrEvent(Event event) {
		dvrEventQueue.add(event);
	}

	public void sendSmsEvent(Event event) {
		smsEventQueue.add(event);
	}

	public void sendHttpEvent(Event event) {
		httpEventQueue.add(event);
	}

	public void sendRfxcomEvent(Event event) {
		rfxcomEventQueue.add(event);
	}

	public synchronized static Freak getInstance() {
		if (freak == null)
			freak = new Freak();

		return freak;
	}

	public boolean isReady() {
		return ready;
	}

	public List<String> getTagList() {
		return eventHandler.getTagList();
	}

	public void clearTags(List<String> tagList) {
		eventHandler.clearTags(tagList);
	}

	private String getIpaddress(String command) {
		List<String> commandList = new ArrayList<String>();
		commandList.add(command);
		ProcessBuilder pb = new ProcessBuilder(commandList);

		pb.redirectErrorStream(false);

		String ipAddress = null;
		try {
			Process process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (ipAddress == null)
					ipAddress = line;

				if (logger.isDebugEnabled())
					logger.debug(line);
			}
			br.close();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If this fails return Unknown
		if (ipAddress == null)
			ipAddress = "Unknown";
		
		return ipAddress;
	}

	private void phoneHome() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Sleep 20 seconds to stabilize
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (logger.isDebugEnabled())
					logger.debug("Try phoning home");

				SettingsConfig settingsConfig = sbtsConfig.getSettingsConfig();
				if (settingsConfig == null)
					return;

				if (settingsConfig.getPhoneHome() == null) {
					logger.debug("No phoneHome setting found, not phoning home");
					return;
				}

				String ipAddress = getIpaddress(SBTS_BASE + "/bin/ipaddress.sh");
				if (logger.isDebugEnabled())
					logger.debug("Phoning home with ipAddress (" + ipAddress
							+ ") to " + settingsConfig.getPhoneHome());
				HttpActionImpl httpAction = new HttpActionImpl("phoneHome",
						"phoneHome", "phoneHome",
						settingsConfig.getPhoneHome(), MethodType.GET, false,
						null, null);
				httpAction.put("ip", ipAddress);
				httpAction.put("version", sbtsConfig.getSettingsConfig()
						.getVersion());
				long eventTime = System.currentTimeMillis();
				freak.sendHttpEvent(new SendActionEvent(httpAction,
						new PhoneHomeTriggerEvent("phoneHome", eventTime),
						eventTime));
			}
		}).start();
	}

	public void subscribeEvents(EventListener listener, String subscription) {
		String finalPattern = null;
		for (String s : subscription.split(",")) {
			String piece = "(?:^" + s.replaceAll("\\*", ".*") + "$)";
			if (finalPattern == null)
				finalPattern = piece;
			else
				finalPattern = finalPattern + "|" +  piece;
		}
		
		Pattern pattern = Pattern.compile(finalPattern);
		listenerMap.put(listener, pattern);
	}
	
	public void unsubscribeEvents(EventListener listener) {
		listener.onEvent(new Notification(true));
		listenerMap.remove(listener);
	}

	public RfxcomHandler getRfxcomHandler() {
		return rfxcomHandler;
	}

	public AtomicBoolean getUpdating() {
		return updating;
	}

	public SbtsDeviceConfig getSbtsConfig() {
		if (sbtsConfig == null) {
			if (logger.isDebugEnabled())
				logger.debug("No variable, initializing");
			reload();
		}
		if (logger.isDebugEnabled())
			logger.debug("In getSbtsConfig");
		return sbtsConfig;
	}

	private void reload() {
		if (logger.isDebugEnabled())
			logger.debug("Reloading context");
		if (SBTS_BASE == null)
			throw new RuntimeException(
					"Structure corrupt, SBTS_BASE not defined");
		try {
			synchronized (freak) {
				sbtsConfig = new SbtsDeviceConfig(SBTS_BASE + "/conf/sbts.xml");
			}
		} catch (Exception e) {
			logger.error("Cannot initialize from config file");
			e.printStackTrace();
		}
	}

	public boolean mountReadonly() {
		ScriptRunner scriptRunner = new ScriptRunner();
		ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
				freak.getSbtsBase() + "/bin/mount_readonly");

		boolean result = scriptRunnerResult.getResult() == 0;
		opLogger.info("Mounting read-only");
		return result;
	}

	public boolean mountReadWrite() {
		ScriptRunner scriptRunner = new ScriptRunner();
		ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
				freak.getSbtsBase() + "/bin/mount_readwrite");

		boolean result = scriptRunnerResult.getResult() == 0;
		opLogger.info("Mounting read/write");
		return result;
	}

	public boolean saveConfig() {
		opLogger.info("Saving configuration");
		if (updating.get()) {
			opLogger.error("Can't save configuration, update in progress");
			return false;
		}

		boolean result = true;
		synchronized (this) {
			try {
				mountReadWrite();
				opLogger.info("Saving configuration");
				if (logger.isDebugEnabled())
					logger.debug("Saving config");

				if (sbtsConfig == null) {
					logger.error("There is no config to save");
					throw new Exception("There is no config to save");
				}

				if (SBTS_BASE == null)
					throw new RuntimeException(
							"Structure corrupt, SBTS_BASE not defined");

				File oldConfig = new File(SBTS_BASE + "/conf/oldsbts.xml");
				oldConfig.delete();
				try {
					for (Action action : sbtsConfig.getActionConfig()
							.getActionList()) {
						if (action.getProfiles() != null) {
							if (logger.isDebugEnabled())
								logger.debug(action.getName());
							for (String s : action.getProfiles())
								if (logger.isDebugEnabled())
									logger.debug("profile: " + s);
						}
					}
					sbtsConfig.outputXML(SBTS_BASE + "/conf/newsbts.xml");
					File currentConfig = new File(SBTS_BASE + "/conf/sbts.xml");
					File newConfig = new File(SBTS_BASE + "/conf/newsbts.xml");
					if (currentConfig.renameTo(oldConfig))
						newConfig.renameTo(currentConfig);
					else
						logger.error("Could not move current config to old and thus could not save config");
				} catch (IOException e) {
					logger.error("Can't save config: " + e.getMessage(), e);
					opLogger.error("Can't save config: " + e.getMessage());
				} catch (TransformerException e) {
					logger.error("Can't save config: " + e.getMessage(), e);
					opLogger.error("Can't save config: " + e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("Can't save config: " + e.getMessage(), e);
					opLogger.error("Can't save config: " + e.getMessage());
				}

				ScriptRunner scriptRunner = new ScriptRunner();
				ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
						SBTS_BASE + "/bin/sync.sh");

				if (scriptRunnerResult.getResult() != 0) {
					logger.error("Error syncing the filesystem");
				}
			} catch (Exception e) {
				opLogger.error("Something went wrong saving the config: "
						+ e.getMessage());
				result = false;
			} finally {
				mountReadonly();
			}
		}

		if (result)
			opLogger.info("Configuration saved");
		else
			opLogger.info("Failed to save configuration");

		return result;
	}

	public String getSbtsBase() {
		return SBTS_BASE;
	}

	public Map<EventListener, Pattern> getListenerMap() {
		return listenerMap;
	}

}
