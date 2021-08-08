package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.ShutdownEvent;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Configuration
public class ContextListener implements ServletContextListener {
	ServletContext context;
	FreakApi freak;

	public void contextInitialized(ServletContextEvent contextEvent) {
		if (freak == null)
			freak = Freak.getInstance();

		freak.start();
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();

		if (freak == null)
			freak = Freak.getInstance();
		if (!freak.isReady())
			return;
		
		freak.sendEvent(new ShutdownEvent());
	}

}
