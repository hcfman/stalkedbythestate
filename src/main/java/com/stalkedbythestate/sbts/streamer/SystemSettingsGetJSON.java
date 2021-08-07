package com.stalkedbythestate.sbts.streamer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.json.DateTimeJSON;
import com.stalkedbythestate.sbts.json.NetworkJSON;
import com.stalkedbythestate.sbts.json.PreferencesJSON;
import com.stalkedbythestate.sbts.json.SystemSettingsJSON;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/systemsettingsgetjson"})
public class SystemSettingsGetJSON extends HttpServlet {
	private static final long serialVersionUID = 527216569117404999L;
	private static final Logger logger = Logger
			.getLogger(SystemSettingsGetJSON.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private NetworkJSON getNetworkSettings() throws Exception {
		ScriptRunner scriptRunner = new ScriptRunner();

		ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
				freak.getSbtsBase() + "/bin/getnetparams.sh", "getnetparams.sh");

		if (scriptRunnerResult.getResult() != 0)
			throw new Exception("Can't retrieve network settings");

		Gson fromGson = new Gson();
		NetworkJSON networkJSON = fromGson.fromJson(
				scriptRunnerResult.getOutput(), NetworkJSON.class);
		if (networkJSON.getNameServer1() == null)
			networkJSON.setNameServer1("");

		if (networkJSON.getNameServer2() == null)
			networkJSON.setNameServer2("");

		if (networkJSON.getNameServer3() == null)
			networkJSON.setNameServer3("");

		return networkJSON;
	}

	private DateTimeJSON getDateTime() throws Exception {
		ScriptRunner scriptRunner = new ScriptRunner();

		ScriptRunnerResult scriptRunnerResult = scriptRunner.spawn(
				freak.getSbtsBase() + "/bin/getdatetime.sh", "getdatetime.sh");

		if (scriptRunnerResult.getResult() != 0) {
			logger.error("getdatetime.sh failed, returned ("
					+ scriptRunnerResult.getResult() + "), output was \""
					+ scriptRunnerResult.getOutput() + "\"");
			throw new Exception("Can't retrieve date/time settings");
		}

		Gson fromGson = new Gson();
		if (logger.isDebugEnabled())
			logger.debug("DateTime data: " + scriptRunnerResult.getOutput());
		DateTimeJSON dateTimeJSON = fromGson.fromJson(
				scriptRunnerResult.getOutput(), DateTimeJSON.class);
		if (logger.isDebugEnabled())
			logger.debug("dateTimeJSON has become: " + dateTimeJSON);
		if (dateTimeJSON.getTimeZone() == null)
			dateTimeJSON.setTimeZone("Europe/London");

		return dateTimeJSON;
	}

	@RequestMapping("/systemsettingsgetjson")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		PrintWriter out = response.getWriter();

		SystemSettingsJSON systemSettingsJSON = new SystemSettingsJSON();

		NetworkJSON networkJSON = null;
		try {
			networkJSON = getNetworkSettings();
		} catch (Exception e) {
			networkJSON.setHostname("sbts");
			networkJSON.setAddress("192.168.1.1");
			networkJSON.setDefaultRoute("192.168.1.254");
			networkJSON.setDhcp(true);
			networkJSON.setMask("255.255.255.0");
			networkJSON.setNameServer1("192.168.1.2");
			networkJSON.setNameServer2("192.168.1.3");
			networkJSON.setNameServer3("192.168.1.4");
		}

		// Set time
		DateTimeJSON dateTimeJSON = new DateTimeJSON();

		try {
			dateTimeJSON = getDateTime();
		} catch (Exception e) {
			logger.error("Caught exception retrieval date/time");
			dateTimeJSON.setUseNtp(true);
			dateTimeJSON.setNtpServer("ntp.ubuntu.com");
			dateTimeJSON.setTimeHour(0);
			dateTimeJSON.setTimeMinute(0);
			dateTimeJSON.setTimeZone("Europe/London");
		}
		systemSettingsJSON.setDateTime(dateTimeJSON);

		TomcatSettings tomcatSettings = new TomcatSettings();
		NetworkJSON tomcatNetworkJSON;
		try {
			tomcatNetworkJSON = tomcatSettings.getSettings();
		} catch (Exception e) {
			logger.error("Can't parse tomcat settings: " + e.getMessage());
			tomcatNetworkJSON = new NetworkJSON();
			tomcatNetworkJSON.setHttpPort(8080);
			tomcatNetworkJSON.setHttpsPort(8443);
			tomcatNetworkJSON.setProtocolDescriptor("HTTP-HTTPS");
		}
		networkJSON.setProtocolDescriptor(tomcatNetworkJSON
				.getProtocolDescriptor());
		networkJSON.setHttpPort(tomcatNetworkJSON.getHttpPort());
		networkJSON.setHttpsPort(tomcatNetworkJSON.getHttpsPort());
		systemSettingsJSON.setNetwork(networkJSON);

		PreferencesJSON preferencesJSON = new PreferencesJSON();
		preferencesJSON.setCleanRate(sbtsConfig.getSettingsConfig()
				.getCleanRate());
		preferencesJSON.setConnectTimeout(sbtsConfig.getSettingsConfig()
				.getConnectTimeout());
		preferencesJSON.setFreeSpace(sbtsConfig.getSettingsConfig()
				.getFreeSpace());
		preferencesJSON
				.setDaysJpeg(sbtsConfig.getSettingsConfig().getDaysMJPG());
		preferencesJSON.setWebPrefix(sbtsConfig.getSettingsConfig()
				.getWebPrefix());
		preferencesJSON.setPhonehomeUrl(sbtsConfig.getSettingsConfig()
				.getPhoneHome());
		systemSettingsJSON.setPreferences(preferencesJSON);

		systemSettingsJSON.setResult(true);

		out.print(gson.toJson(systemSettingsJSON));
	}

}
