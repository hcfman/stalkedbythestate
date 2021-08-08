package com.stalkedbythestate.sbts.email;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.SendActionEvent;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.EmailActionImpl;
import com.stalkedbythestate.sbts.eventlib.EventType;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EmailHandler {
	private static final Logger logger = Logger.getLogger(EmailHandler.class);
	private FreakApi freak;
	private LinkedBlockingQueue<Event> eventQueue;
	private SbtsDeviceConfig sbtsConfig;
	private String emailTemplate;

	public EmailHandler(FreakApi freak, LinkedBlockingQueue<Event> eventQueue) {
		this.freak = freak;
		this.eventQueue = eventQueue;

		sbtsConfig = freak.getSbtsConfig();
	}

	private void initializeTemplate() {
		String templateFile = "emailTemplate.html";
		java.net.URL url = Thread.currentThread().getContextClassLoader()
				.getResource(templateFile);
		try {
			InputStream is = url.openStream();

			// Leave it as null, send plain E-mail then
			if (is == null)
				return;

			BufferedReader inStream = new BufferedReader(new InputStreamReader(
					is));
			char buffer[] = new char[1000];
			StringBuilder sb = new StringBuilder();

			try {
				int len;
				while ((len = inStream.read(buffer, 0, buffer.length)) > 0) {
					sb.append(buffer, 0, len);
					if (logger.isDebugEnabled())
						logger.debug("Template read " + len);
				}

				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			emailTemplate = sb.toString();

		} catch (Exception e) {
			logger.error("Cannot initialize E-mail template: ", e);
		}

	}

	public void start() {
		initializeTemplate();

		Thread thread = new Thread() {

			@Override
			public void run() {
				if (logger.isDebugEnabled())
					logger.debug("Starting email handler");
				int numThreads = sbtsConfig.getSettingsConfig()
						.getEmailThreads();

				Executor executor = Executors.newFixedThreadPool(numThreads);

				while (true) {
					Event event = null;
					try {
						if (logger.isDebugEnabled())
							logger.debug("Try and take an Email event");
						event = eventQueue.take();
						if (logger.isDebugEnabled())
							logger.debug("Taken an email event: " + event);
						if (event.getEventType() == EventType.EVENT_SHUTDOWN)
							return;

						switch (event.getEventType()) {
						case EVENT_CONFIGURE:
							break;
						case EVENT_SHUTDOWN:
							return;
						case EVENT_ACTION:
							if (logger.isDebugEnabled())
								logger.debug("Process EVENT_ACTION");
							if (!(event instanceof SendActionEvent))
								break;
							if (logger.isDebugEnabled())
								logger.debug("Correct type for an Email");

							SendActionEvent sendActionEvent = (SendActionEvent) event;
							Action action = sendActionEvent.getAction();
							if (!(action instanceof EmailActionImpl))
								break;
							if (logger.isDebugEnabled())
								logger.debug("Action is of correct email type, executing");

							executor.execute(new HandleEmail(freak, event,
									sendActionEvent.getOriginalEvent(), action,
									emailTemplate));
							break;
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (logger.isDebugEnabled())
						logger.debug("Got event: " + event);
				}
			}
		};

		thread.setName("email-handler");
		thread.start();

	}

}
