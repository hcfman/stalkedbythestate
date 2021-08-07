package com.stalkedbythestate.sbts.sbtsdevice.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;

public class CreateSynthTriggerConfig {

	public static void addTriggerConfig(Document doc, Element parent, Collection<SynthTrigger> triggers) {
		for (SynthTrigger trigger : triggers) {
			parent.appendChild(doc.createTextNode("\n\t"));

			Element SynthThriggerElement = (Element) doc.createElement("synthTrigger");
			parent.appendChild(SynthThriggerElement);

			SynthThriggerElement.appendChild(doc.createTextNode("\n\t\t"));
			Element resultElement = doc.createElement("result");
			SynthThriggerElement.appendChild(resultElement);

			SynthThriggerElement.appendChild(doc.createTextNode("\n\t\t"));
			Element withinSecondsElement = doc.createElement("withinSeconds");
			SynthThriggerElement.appendChild(withinSecondsElement);
			withinSecondsElement.setTextContent(Integer.toString(trigger.getWithinSeconds()));

			
			resultElement.setTextContent(trigger.getResult());
			
			Collection<String> triggerEventNames = trigger.getTriggerEventNames();
			for (String eventName : triggerEventNames) {
				SynthThriggerElement.appendChild(doc.createTextNode("\n\t\t"));
				Element eventNameElement = doc.createElement("eventName");
				SynthThriggerElement.appendChild(eventNameElement);
				eventNameElement.appendChild(doc.createTextNode(eventName));
			}
			SynthThriggerElement.appendChild(doc.createTextNode("\n\t"));

		}
		parent.appendChild(doc.createTextNode("\n"));

	}

}
