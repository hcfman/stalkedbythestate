package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.RfxcomTriggerEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.rfxcomlib.Packet;
import com.stalkedbythestate.sbts.rfxcomlib.RfxComListener;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomCommand;
import com.stalkedbythestate.sbts.rfxcomlib.RfxcomOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RfxComListenerImpl implements RfxComListener {
	private static final Logger logger = LoggerFactory
			.getLogger(RfxComListenerImpl.class);
	private static final Logger rfxcomLogger = LoggerFactory.getLogger("rfxcom");
	private RfxcomCommand command;
	private FreakApi freak;

	public RfxComListenerImpl(RfxcomCommand command, FreakApi freak) {
		this.command = command;
		this.freak = freak;
	}

	public RfxComListenerImpl(RfxcomCommand command) {
		this.command = command;
	}

	public String stringRepresentationDecimal(Packet packet) {
		StringBuffer sb = new StringBuffer();
		sb.setLength(0);
		int length = packet.getLength();
		int intArray[] = packet.getIntArray();
		for (int i = 0; i < length; i++) {
			if (i > 0)
				sb.append(",");
			sb.append(Integer.toString(intArray[i]));
		}

		return sb.toString();
	}

	public void fire(Packet packet) {
		freak.sendEvent(new RfxcomTriggerEvent(command.getEventName(), System
				.currentTimeMillis(), stringRepresentationDecimal(packet)));
	}

	public boolean matches(Packet packet) {
		int[] packetInts = packet.getIntArray();
		int[] mask = command.getMask();
		int[] packetValues1 = command.getPacketValues1();
		int[] packetValues2 = command.getPacketValues2();
		RfxcomOperator[] operators = command.getOperator();

		if (packet.getLength() < operators.length)
			return false;

		boolean matches = true;
		int index = 0;
		for (RfxcomOperator operator : operators) {
			switch (operator) {
			case EQ:
				if ((packetInts[index] & mask[index]) != packetValues1[index])
					return false;
				break;
			case GE:
				if ((packetInts[index] & mask[index]) < packetValues1[index])
					return false;
				break;
			case GT:
				if ((packetInts[index] & mask[index]) <= packetValues1[index])
					return false;
				break;
			case LE:
				if ((packetInts[index] & mask[index]) > packetValues1[index])
					return false;
				break;
			case LT:
				if ((packetInts[index] & mask[index]) >= packetValues1[index])
					return false;
				break;
			case RANGE:
				if ((packetInts[index] & mask[index]) < packetValues1[index]
						|| (packetInts[index] & mask[index]) > packetValues2[index])
					return false;
				break;
			}

			index++;
		}

		return matches;
	}

	public RfxcomCommand getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return "RfxComListenerImpl [command=" + command + "]";
	}

}
