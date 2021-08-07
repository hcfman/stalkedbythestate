package com.stalkedbythestate.sbts.rfxcomhandler;

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.rfxcomlib.*;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class RfxcomController {
	private static final Logger logger = Logger
			.getLogger(RfxcomController.class);
	private static final Logger rfxcomLogger = Logger.getLogger("rfxcom");
	private volatile Set<RfxComListener> eventSet = new HashSet<RfxComListener>();
	private Map<String, Long> lastTriggeredMap = new HashMap<String, Long>();
	private SystemStatusListener statusListener;
	private SystemStatusListener newStatusListener;
	private FreakApi freak;
	private AtomicBoolean connected = new AtomicBoolean(false);
	private FileInputStream fis;
	private FileOutputStream fos;
	private StringBuffer sb = new StringBuffer();
	private PacketUtils packetUtils = new PacketUtils();

	public RfxcomController(FreakApi freak) {
		logger.debug("Creating RfxcomController");
		this.freak = freak;
	}

	private void notifySubscribers(PacketReader packetReader, Packet packet) {
		sb.setLength(0);
		int matchCount = 0;
		logger.debug("Notifying subscribers");
		long now = System.currentTimeMillis();
		for (RfxComListener listener : eventSet) {
			if (listener.matches(packet)) {
				String commandName = listener.getCommand().getName();
				int hysteresis = listener.getCommand().getHysteresis();
				Long lastFired = lastTriggeredMap.get(commandName);
				if (hysteresis == 0 || lastFired == null
						|| now - lastFired > hysteresis) {
					listener.fire(packet);
					lastTriggeredMap.put(commandName, now);
				}
				
				logger.debug("Packet matches: "
						+ listener.getCommand().getName());

				if (matchCount > 0)
					sb.append(", ");
				sb.append(listener.getCommand().getName());
				// freak.sendEvent(new RfxcomTriggerEvent(listener.getCommand()
				// .getEventName(), System.currentTimeMillis()));
				
				matchCount++;
			} else {
				logger.debug("Packet doesn't match");
			}
		}
		
		if (!connected.get()) {
			setConnected(true);
			rfxcomLogger.info("Received a packet, setting connected!");
		}
			
		rfxcomLogger.info("Packet "
				+ packetUtils.packetToString(packet.getIntArray())
				+ (matchCount > 0 ? (" matches(" + sb.toString() + ")") : ""));
	}

	public void subscribe(RfxComListener rfxComListener) {
		logger.info("Subscribing to: " + rfxComListener.toString());
		eventSet.add(rfxComListener);
	}

	public void clearAllSubscriptions() {
		lastTriggeredMap = new HashMap<String, Long>();
		Set<RfxComListener> newEventSet = new HashSet<RfxComListener>();
		if (statusListener != null) {
			newEventSet.add(statusListener);
		}
		eventSet = newEventSet;
	}

	public boolean isConnected() {
		return connected.get();
	}

	public void setConnected(boolean connected) {
		this.connected.set(connected);
	}

	public String categorize(Packet packet) {
		return "";
	}

	private void subscribeToStatusCommand() {
		// 01, 00, 01, 02, 53, 29, 00, 0E, 6F, 01, 00, 00, 00
		// And the new one
		// 00, 00, 27, 00, 01
		RfxcomCommand statusCommand = new RfxcomCommand("SYSTEM_STATUS",
				"Used internally", RfxcomType.GENERIC_INPUT);
		RfxcomCommand newStatusCommand = new RfxcomCommand("NEW_SYSTEM_STATUS",
				"Used internally", RfxcomType.GENERIC_INPUT);
		int[] intPacketValues1 = new int[13];
		int[] intPacketValues2 = new int[13];
		int[] intMask = new int[13];
		RfxcomOperator operators[] = new RfxcomOperator[13];

		int[] newIntPacketValues1 = new int[5];
		int[] newIntPacketValues2 = new int[5];
		int[] newIntMask = new int[5];
		RfxcomOperator newOperators[] = new RfxcomOperator[5];

		// Set to ignore all bytes
		for (int i = 0; i < 13; i++) {
			intPacketValues1[0] = 0x00;
			intPacketValues2[0] = 0x00;
			intMask[i] = 0x00;
			operators[i] = RfxcomOperator.EQ;
		}

		// Now just match the 1st two bytes
		intMask[0] = 0xFF;
		intMask[1] = 0xFF;
		intPacketValues1[0] = 0x01;
		intPacketValues2[0] = 0x01;
		intPacketValues1[1] = 0x00;
		intPacketValues2[1] = 0x00;

		statusCommand.setHysteresis(0);
		statusCommand.setMask(intMask);
		statusCommand.setPacketValues1(intPacketValues1);
		statusCommand.setPacketValues2(intPacketValues2);
		statusCommand.setOperator(operators);

		statusListener = new SystemStatusListener(this, statusCommand);
		subscribe(statusListener);
		
		// Now subscribe to the new version as well
		// Set to ignore all bytes
		for (int i = 0; i < 5; i++) {
			newIntPacketValues1[i] = 0x00;
			newIntPacketValues2[i] = 0x00;
			newIntMask[i] = 0x00;
			newOperators[i] = RfxcomOperator.EQ;
		}

		// Now just match all 5 bytes
		newIntMask[0] = 0xFF;
		newIntMask[1] = 0xFF;
		newIntMask[2] = 0xFF;
		newIntMask[3] = 0xFF;
		newIntMask[4] = 0xFF;
		newIntPacketValues1[0] = 0x00;
		newIntPacketValues1[1] = 0x00;
		newIntPacketValues1[2] = 0x27;
		newIntPacketValues1[3] = 0x00;
		newIntPacketValues1[4] = 0x01;
		newIntPacketValues2[0] = 0x00;
		newIntPacketValues2[1] = 0x00;
		newIntPacketValues2[2] = 0x27;
		newIntPacketValues2[3] = 0x00;
		newIntPacketValues2[4] = 0x01;

		newStatusCommand.setHysteresis(0);
		newStatusCommand.setMask(newIntMask);
		newStatusCommand.setPacketValues1(newIntPacketValues1);
		newStatusCommand.setPacketValues2(newIntPacketValues2);
		newStatusCommand.setOperator(newOperators);

		newStatusListener = new SystemStatusListener(this, newStatusCommand);
		logger.debug("Subscribing to new status message");
		subscribe(newStatusListener);
	}

	public void sendRfxcomCommand(int[] intArray) {
		if (!isConnected())
			return;

		byte[] byteArray = new byte[intArray.length + 1];

		if (intArray.length > 254) {
			logger.error("Packet too large (" + intArray.length + "), aborting");
			return;
		}

		for (int i = 0; i < intArray.length; i++) {
			int value = intArray[i];
			// Ignore if anything is too big
			if (value > 256) {
				logger.error("Int value is too large (" + i
						+ ") to send, aborting send");
				return;
			}

			try {
				if (value > 127)
					byteArray[i + 1] = (byte) (value - 256);
				else
					byteArray[i + 1] = (byte) value;
			} catch (Exception e) {
				logger.error("Failed to convert int(" + value
						+ ") to byte, aborting send", e);
				return;
			}
		}

		int len = byteArray.length - 1;
		if (len > 127) {
			byteArray[0] = (byte) (256 - len);
		} else {
			byteArray[0] = (byte) len;
		}

		if (byteArray.length > 3)
			byteArray[3] = 0x00;

		if (isConnected()) {
			rfxcomLogger.info("Sending " + packetUtils.packetToString(byteArray));
			try {
				fos.write(byteArray);
			} catch (IOException e) {
				logger.error("I/O Error sending RFXCOM command", e);
			}
		} else {
			rfxcomLogger
					.info("RFXCOM is not yet connected, not sending command");
		}
	}

	public void start(FileInputStream fis, FileOutputStream fos)
			throws FileNotFoundException {
		this.fis = fis;
		this.fos = fos;

		// You need to receive one of these to consider the system connected
		subscribeToStatusCommand();

		Packet packet;
		PacketReader packetReader = new PacketReader(fis, fos);

		rfxcomLogger.info("Starting RFXCOM");

		while (true) {
			try {
				packet = packetReader.getPacket();
			} catch (IOException e) {
				connected.set(false);
				rfxcomLogger.info("RFXCOM stopped");
				if (statusListener != null)
					eventSet.remove(statusListener);
				return;
			}
			logger.debug("Got a packet");
			if (packet != null)
				notifySubscribers(packetReader, packet);
		}
	}

}
