package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomOperator;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomType;
import org.apache.log4j.Logger;

public class RfxcomCommandJSON {
	private static final Logger logger = Logger
			.getLogger(RfxcomCommandJSON.class);
	private String name;
	private String description;
	private String eventName;
	private int hysteresis;
	private String rfxcomType;
	private String[] packetValues1;
	private String[] packetValues2;
	private String[] packetMask;
	private String[] packetOperator;

	public RfxcomCommandJSON() {
	}
	
	public RfxcomCommand toRfxcomDevice() {
		RfxcomType rfxComrfxcomType = RfxcomType.set(rfxcomType.replaceAll(
				"\\s+", "_"));
		RfxcomCommand rfxcomCommand = new RfxcomCommand(name, description,
				rfxComrfxcomType);

		int len = packetValues1.length;

		int[] intPacketValues1 = new int[len];
		int[] intPacketValues2 = new int[len];
		int[] intMask = new int[len];
		RfxcomOperator operators[] = new RfxcomOperator[len];
		if (rfxComrfxcomType == RfxcomType.GENERIC_INPUT) {
			rfxcomCommand.setEventName(eventName);
			rfxcomCommand.setHysteresis(hysteresis);
		} else {

		}

		for (int i = 0; i < len; i++) {
			try {
				intPacketValues1[i] = Integer.parseInt(packetValues1[i], 16);
			} catch (NumberFormatException e) {
				logger.error("Failed to parse packetValue1[" + packetValues1[i]
						+ "]", e);
				intPacketValues1[i] = 0;
			}
		}

		switch (rfxComrfxcomType) {
		case GENERIC_INPUT:
			for (int i = 0; i < len; i++) {
				String operator = packetOperator[i];
				try {
					if (operator.equals("RANGE"))
						intPacketValues2[i] = Integer.parseInt(
								packetValues2[i], 16);
					else
						intPacketValues2[i] = 0;
				} catch (NumberFormatException e1) {
					logger.error("Failed to convert packetValues2["
							+ packetValues2[i] + "]", e1);
					intPacketValues2[i] = 0;
				}
				try {
					intMask[i] = Integer.parseInt(packetMask[i], 16);
				} catch (NumberFormatException e) {
					logger.error("Failed to convert mask(" + packetMask[i]
							+ ")" + e.getMessage());
					intMask[i] = 0;
				}
				operators[i] = RfxcomOperator.set(operator);
//				System.out.println("intMask[" + i + "]: " + intMask[i]);
			}

			rfxcomCommand.setPacketValues1(intPacketValues1);
			rfxcomCommand.setPacketValues2(intPacketValues2);
			rfxcomCommand.setMask(intMask);
			rfxcomCommand.setOperator(operators);
			break;
		case GENERIC_OUTPUT:
			rfxcomCommand.setPacketValues1(intPacketValues1);
			break;
		}

//		System.out.println("rfxcomCommand: " + rfxcomCommand);
		return rfxcomCommand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public String[] getPacketValues1() {
		return packetValues1;
	}

	public void setPacketValues1(String[] packetValues1) {
		this.packetValues1 = packetValues1;
	}

	public String[] getPacketValues2() {
		return packetValues2;
	}

	public void setPacketValues2(String[] packetValues2) {
		this.packetValues2 = packetValues2;
	}

	public String[] getPacketMask() {
		return packetMask;
	}

	public void setPacketMask(String[] packetMask) {
		this.packetMask = packetMask;
	}

	public String[] getPacketOperator() {
		return packetOperator;
	}

	public void setPacketOperator(String[] packetOperator) {
		this.packetOperator = packetOperator;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRfxcomType() {
		return rfxcomType;
	}

	public void setRfxcomType(String rfxcomType) {
		this.rfxcomType = rfxcomType;
	}

	public int getHysteresis() {
		return hysteresis;
	}

	public void setHysteresis(int hysteresis) {
		this.hysteresis = hysteresis;
	}

}
