package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;

public class CreateWatchdogConfig {

	public static void addTriggerConfig(Document doc, Element parent, Collection<Watchdog> triggers) {
		for (Watchdog trigger : triggers) {
			parent.appendChild(doc.createTextNode("\n\t"));

			Element watchdogElement = (Element) doc.createElement("watchdog");
			parent.appendChild(watchdogElement);

			watchdogElement.appendChild(doc.createTextNode("\n\t\t"));
			Element resultElement = doc.createElement("result");
			watchdogElement.appendChild(resultElement);

			watchdogElement.appendChild(doc.createTextNode("\n\t\t"));
			Element withinSecondsElement = doc.createElement("withinSeconds");
			watchdogElement.appendChild(withinSecondsElement);
			withinSecondsElement.setTextContent(Long.toString(trigger.getWithinSeconds()));

			
			resultElement.setTextContent(trigger.getResult());
			
			Collection<String> triggerEventNames = trigger.getTriggerEventNames();
			for (String eventName : triggerEventNames) {
				watchdogElement.appendChild(doc.createTextNode("\n\t\t"));
				Element eventNameElement = doc.createElement("eventName");
				watchdogElement.appendChild(eventNameElement);
				eventNameElement.appendChild(doc.createTextNode(eventName));
			}
			watchdogElement.appendChild(doc.createTextNode("\n\t"));

		}
		parent.appendChild(doc.createTextNode("\n"));

	}

}
