package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomOperator;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

public class RfxcomConfig {
	private Map<String, RfxcomCommand> commands = new HashMap<String, RfxcomCommand>();

	public Map<String, RfxcomCommand> getCommands() {
		return commands;
	}

	public List<RfxcomCommand> getcommandsAsList() {
		List<RfxcomCommand> commandList = new ArrayList<RfxcomCommand>(
				getCommands().values());
		Collections.sort(commandList, new Comparator<RfxcomCommand>() {

			@Override
			public int compare(RfxcomCommand o1, RfxcomCommand o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return commandList;
	}

	public void setCommands(Map<String, RfxcomCommand> commands) {
		this.commands = commands;
	}

	private String intArrayToHex(int[] intArray) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (int value : intArray) {
			if (count > 0)
				sb.append(",");
			sb.append(String.format("%02X", value));
			count++;
		}
		return sb.toString();
	}

	public String[] intArrayToHexStringArray(int[] intArray) {
		String[] hexStringArray = new String[intArray.length];
		int count = 0;
		for (int value : intArray) {
			hexStringArray[count] = String.format("%02X", value);
			count++;
		}
		return hexStringArray;
	}

	private String operatorArrayToString(RfxcomOperator[] operators) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (RfxcomOperator operator : operators) {
			if (count > 0)
				sb.append(",");
			sb.append(operator.toString());
			count++;
		}
		return sb.toString();
	}

	public String[] operatorToStringArray(RfxcomOperator[] operators) {
		String[] stringArray = new String[operators.length];
		int count = 0;
		for (RfxcomOperator operator : operators) {
			stringArray[count] = operator.toString();
			count++;
		}
		return stringArray;
	}

	public void addRfxcomConfig(Document doc, Element parent) {
		Element rfxcomElement = doc.createElement("rfxcom");
		parent.appendChild(rfxcomElement);

		parent.appendChild(doc.createTextNode("\n\n"));

		if (commands.isEmpty())
			return;

		for (RfxcomCommand command : getcommandsAsList()) {
			rfxcomElement.appendChild(doc.createTextNode("\n\t"));
			Element commandElement = doc.createElement("command");
			rfxcomElement.appendChild(commandElement);
			commandElement.setAttribute("name", command.getName());
			commandElement
					.setAttribute("description", command.getDescription());
			commandElement.setAttribute("type", command.getRfxcomType()
					.toString());

			if (command.getRfxcomType() == RfxcomType.GENERIC_INPUT) {
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				Element eventNameElement = doc.createElement("eventName");
				eventNameElement.appendChild(doc.createTextNode(command
						.getEventName()));
				commandElement.appendChild(eventNameElement);

				Element hysteresisElement = doc.createElement("hysteresis");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(hysteresisElement);
				hysteresisElement.appendChild(doc.createTextNode(Integer
						.toString(command.getHysteresis())));

				Element values1Element = doc.createElement("values1");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(values1Element);
				values1Element.appendChild(doc
						.createTextNode(intArrayToHex(command
								.getPacketValues1())));

				Element values2Element = doc.createElement("values2");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(values2Element);
				values2Element.appendChild(doc
						.createTextNode(intArrayToHex(command
								.getPacketValues2())));

				Element maskElement = doc.createElement("mask");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(maskElement);
				maskElement.appendChild(doc
						.createTextNode(intArrayToHex(command.getMask())));

				Element operatorElement = doc.createElement("operator");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(operatorElement);
				operatorElement.appendChild(doc
						.createTextNode(operatorArrayToString(command
								.getOperator())));
			} else {
				Element values1Element = doc.createElement("values1");
				commandElement.appendChild(doc.createTextNode("\n\t\t"));
				commandElement.appendChild(values1Element);
				values1Element.appendChild(doc
						.createTextNode(intArrayToHex(command
								.getPacketValues1())));

			}

			commandElement.appendChild(doc.createTextNode("\n\t"));

			rfxcomElement.appendChild(doc.createTextNode("\n"));
		}

	}

	@Override
	public String toString() {
		return "RfxcomConfig [commands=" + commands + "]";
	}

}
