package com.stalkedbythestate.sbts.cleanerhandler;

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.DiskState;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class CleanerHandler {
	private static final Logger logger = Logger.getLogger(CleanerHandler.class);
	private FreakApi freak;
	private LinkedBlockingQueue<Event> eventQueue;
	private SbtsDeviceConfig sbtsConfig;
	private Timer timer = null;

	public CleanerHandler(FreakApi freak, LinkedBlockingQueue<Event> eventQueue) {
		if (logger.isDebugEnabled())
			logger.debug("Constructed CleanerHandler");
		this.freak = freak;
		this.eventQueue = eventQueue;

	}

	private void runCleaner() {
		sbtsConfig = freak.getSbtsConfig();

		timer = new Timer();
		if (logger.isDebugEnabled())
			logger.debug("Scheduling cleaner");
		if (logger.isDebugEnabled())
			logger.debug("cleanRate: "
					+ sbtsConfig.getSettingsConfig().getCleanRate());
		if (logger.isDebugEnabled())
			logger.debug("Want to schedule at: "
					+ sbtsConfig.getSettingsConfig().getCleanRate() * 60000);
		timer.scheduleAtFixedRate(new CleanerTask(), sbtsConfig
				.getSettingsConfig().getCleanRate() * 60000, sbtsConfig
				.getSettingsConfig().getCleanRate() * 60000);
	}

	private void spawnCleaner() {
		if (sbtsConfig.getDiskConfig().getDiskState() != DiskState.ALL_GOOD)
			return;

		List<String> command = new ArrayList<String>();

		command.add(freak.getSbtsBase() + "/bin/cleanSpace.sh");
		if (logger.isDebugEnabled())
			logger.debug("Cleaner command: " + freak.getSbtsBase()
					+ "/bin/cleanSpace.sh");
		command.add(sbtsConfig.getSettingsConfig().getFreeSpace().toString());
		command.add(sbtsConfig.getSettingsConfig().getDaysMJPG().toString());

		if (!sbtsConfig.getSettingsConfig().isCheckMount())
			command.add("false");

		StringBuilder sb = new StringBuilder();
		for (String c : command)
			sb.append(c + " ");

		logger.debug("Command = " + sb.toString());

		ProcessBuilder pb = new ProcessBuilder(command);

		pb.redirectErrorStream(true);

		try {
			Process process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
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

	}

	private class CleanerTask extends TimerTask {

		@Override
		public void run() {
			if (logger.isDebugEnabled())
				logger.debug("CLEANING");
			timer.cancel();
			spawnCleaner();
			runCleaner();
		}

	}

	public void start() {
		if (logger.isDebugEnabled())
			logger.debug("Starting cleaner scheduler");
		runCleaner();
	}
}
