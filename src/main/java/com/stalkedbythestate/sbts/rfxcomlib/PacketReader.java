package com.stalkedbythestate.sbts.rfxcomlib;

// Copyright (c) 2021 Kim Hendrikse

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class PacketReader {
	private static final Logger logger = Logger.getLogger(PacketReader.class);
	private static final Logger rfxcomLogger = Logger.getLogger("rfxcom");
	private FileInputStream fis;
	private FileOutputStream fos;
	private int globalOffset;
	private Set<Integer> validLengthSet = new HashSet<Integer>();
	private long lastReadTime = 0;
	private long packetResetTimeLimit = 1;
	private int length;
	private byte[] buffer;
	private int bufferOffset = 0;
	private Packet packet;
	private StringBuffer sb = new StringBuffer();

	// Packet (hex 0D 00 00 00 00 00 00 00 00 00 00 00 00 00)
	private byte[] resetCommand = new byte[] { 0x0D, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	// 0D 00 00 01 02 00 00 00 00 00 00 00 00 00
	private byte[] statusRequest = new byte[] { 0x0D, 0x00, 0x00, 0x01, 0x02,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00

	};

	private void initialize() {
//		System.out.println("Initializing");
		validLengthSet.add(0x04);
		validLengthSet.add(0x05);
		validLengthSet.add(0x06);
		validLengthSet.add(0x07);
		validLengthSet.add(0x08);
		validLengthSet.add(0x09);
		validLengthSet.add(0x10);
		validLengthSet.add(0x11);
		validLengthSet.add(0x0A);
		validLengthSet.add(0x0B);
		validLengthSet.add(0x0C);
		validLengthSet.add(0x0D);
		// Test length
		// validLengthSet.add(44);

		reset();
	}

	public PacketReader() {
		initialize();
	}

	public PacketReader(FileInputStream fis, FileOutputStream fos) {
		this.fis = fis;
		this.fos = fos;

		initialize();
	}

	public PacketReader(FileInputStream fis, FileOutputStream fos,
                        long packetResetTimeLimit) {
		this.fis = fis;
		this.fos = fos;
		this.packetResetTimeLimit = packetResetTimeLimit;

		initialize();
	}

	private int readMe(FileInputStream fis, byte[] byteArray, int offset,
                       int amount) throws IOException {
		// Read from buffer if it exists
//		if (buffer != null)
//			System.out.println("readMe stats bufferOffset: " + bufferOffset
//					+ " buffer.length:" + buffer.length);
		int bufferLength;
		if (buffer != null && (bufferLength = buffer.length - bufferOffset) > 0) {
			int amountToCopy;
			if (bufferLength >= amount)
				amountToCopy = amount;
			else
				amountToCopy = bufferLength;

			for (int i = 0; i < amountToCopy; i++)
				byteArray[i] = buffer[bufferOffset + i];

//			System.out.println("readMe[from buffer](offset:" + bufferOffset
//					+ ", amount:" + amountToCopy + ")");
			bufferOffset += amountToCopy;

			return amountToCopy;
		}

		// Reading from FileInputStream
//		System.out.println("Calling read system call readMe(buffer, " + offset + ", " + amount + ")");
		int amountRead = fis.read(byteArray, offset, amount);
//		System.out.println("Returning from read system call Asked for " + amount + ", got " + amountRead);
		if (amountRead > 0) {
//			System.out.println(globalOffset
//					+ " readMe("
//					+ Arrays.toString(Arrays.copyOfRange(byteArray, offset,
//							offset + amountRead)) + ") = " + amountRead);
			globalOffset += amountRead;
		} else {
			throw new IOException("Read failed to return data, must have been interrupted");
		}

//		System.out.println("readMe returning " + amountRead);
//		System.out.println("");
		return amountRead;
	}

	private boolean getLength() throws IOException {
		byte[] byteArray = new byte[257];

		int amountRead;
		while ((amountRead = readMe(fis, byteArray, 0, 1)) >= 0) {
			if (amountRead < 0) {
				logger.debug("Received EOF whilst waiting for packet length byte");
				throw new IOException(
						"Received EOF whilst waiting for packet length byte");
			}
			// If it was a broken read, it would have thrown an Exception
			if (amountRead == 0)
				continue;

			if (byteArray[0] < 0)
				length = 256 - byteArray[0];
			else
				length = byteArray[0];

			if (!validLengthSet.contains(length)) {
				lastReadTime = System.currentTimeMillis();
				continue;
			} else {
				break;
			}
		}

		return true;
	}

	// Timed out reading body, resetting packet with [11, 17, 0, 22, 0, 88, -1,
	// -90, 1, 1, 15, 112, 11, 17, 0, 23, 0, 88, -1, -90, 1, 1, 15, 112, 11, 17,
	// 0]
	// In getLength()
	// Got a length byte
	// Length = 11
	// Is a valid length
	// Got a header length good
	//
	// In getBody
	// Construct new packet
	// Construct packet of length: 11
	// Full packet is: [11, 17, 0, 22, 0, 88, 255, 166, 1, 1, 15]
	// In getLength()
	// Got a length byte
	// Length = 11
	// Is a valid length
	// Got a header length good

	private boolean getBody() throws IOException {
		lastReadTime = System.currentTimeMillis();
		byte[] byteArray = new byte[257];
		int amountRead = 0;

		int bodyOffset = 0;
		int amountToRead = length;
		int currentLength = 0;
		while (amountToRead > 0
				&& (amountRead = readMe(fis, byteArray, bodyOffset,
						amountToRead)) >= 0) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - lastReadTime) / 1000 > packetResetTimeLimit) {
				if (amountRead > 0) {
					currentLength += amountRead;
					System.out
							.println("Timed out reading body, resetting packet with "
									+ Arrays.toString(Arrays.copyOfRange(
											byteArray, 0, currentLength)));

					buffer = Arrays.copyOfRange(byteArray, 0, currentLength);
					bufferOffset = 0;
				} else {
					System.out
							.println("Timed out reading body, resetting packet, no bytes read");
				}
				return false;
			}
			lastReadTime = currentTime;

			amountToRead -= amountRead;
			bodyOffset += amountRead;
			currentLength += amountRead;

//			System.out.println("Constructing body, was able to read "
//					+ amountRead
//					+ " bytes "
//					+ Arrays.toString(Arrays.copyOfRange(byteArray, 0,
//							currentLength)));
		}
		if (amountRead < 0) {
			logger.debug("Received EOF whilst reading packet body");
			throw new IOException("Received EOF whilst reading packet body");
		}

//		System.out.println("Construct new packet");
		packet = new Packet(byteArray, length);
		return true;
	}

	private void flush() {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				byte[] byteArray = new byte[20];
				int amountAvail;

				// Read if bytes available
				long startTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - startTime < 1000) {
					while ((amountAvail = fis.available()) > 0) {
						if (amountAvail > 20)
							amountAvail = 20;
						readMe(fis, byteArray, 0, amountAvail);
					}

					Thread.sleep(800);
				}

				return null;
			}
		};

		Future<Integer> future = executor.submit(callable);
		try {
			@SuppressWarnings("unused")
			int dump = future.get(1, TimeUnit.SECONDS);
			executor.shutdownNow();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			executor.shutdownNow();
			logger.debug("Timed out...");
			// e.printStackTrace();
		}
	}

	private void reset() {
		logger.debug("Resetting...");
		try {
			logger.debug("Writing " + resetCommand.length
					+ " bytes to reset");
			fos.write(resetCommand, 0, resetCommand.length);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Flush the input, trying for one second
			flush();

			logger.debug("Send status request...");

			fos.write(statusRequest, 0, resetCommand.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Packet getPacket() throws IOException {
		if (!getLength()) {
			return null;
		}
		if (length == 0)
			throw new IOException("Packet with length 0 found, terminating");

		if (!getBody())
			return null;

		return packet;
	}

}
