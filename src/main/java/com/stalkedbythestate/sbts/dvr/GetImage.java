package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

public class GetImage {

	boolean findBoundary(BufferedInputStream inStream, String boundary)
			throws IOException {
		String line;

		do {
			line = readLine(inStream);
			if (line == null)
				return false;
			// System.out.println("Looking for a boundary, got line \"" + line
					// + "\"");
		} while (!line.startsWith(boundary) && !line.startsWith( "--" + boundary ));

		// System.out.println("Found a boundary with line + \"" + line + "\"");

		return true;
	}

	String readLine(BufferedInputStream inStream) throws IOException {

		int c;
		StringBuffer s = new StringBuffer();

		while ((c = inStream.read()) != -1 && c != '\n') {
			s.append((char) c);
		}
		if (c == -1)
			return null;

		return s.toString();

	}

	ImageBuffer getImage(BufferedReader bin, BufferedInputStream inStream,
                         String boundary) throws IOException {

		if (!findBoundary(inStream, boundary))
			return null;

		int contentLength = 0;
		String rawline;
		while ((rawline = readLine(inStream)) != null) {
			String line = rawline.trim().toLowerCase();
			// System.out.println("Reading header lines, got \"" + line +
			// "\"\n");

			if (line.equals("")) {
				// System.out.println("Got blank line, skip and read image");
				break;
			}

			int lengthIndex;
			if ((lengthIndex = line.indexOf("content-length:")) >= 0) {
				try {
					/*
					 * System.out.println("Found content length header at offset "
					 * + lengthIndex); System.out.println("Want a number from "
					 * + line.substring(15).trim());
					 */
					contentLength = Integer.parseInt(line.substring(
							lengthIndex + 15).trim());
				} catch (NumberFormatException e) {
					throw new IOException(e.getMessage());
				}
				// System.out.println("Found Content length = " +
				// contentLength);
			}

		}

		if (contentLength < 2 || contentLength > 500000)
			return null;

		// System.out.println("contentLength " + contentLength + " bytes");

		int bytesRead = 0;
		int bytesToRead = contentLength;
		byte image[] = new byte[contentLength];
		while (bytesToRead > 0) {
			int n = inStream.read(image, bytesRead, bytesToRead);
			// System.out.println("Read " + n + " bytes ");
			if (n <= 0)
				System.exit(0);
			bytesRead += n;
			bytesToRead -= n;
			// System.out.println("Remaining bytes " + bytesToRead);
		}

		long timestamp = System.currentTimeMillis();

		return new ImageBuffer(timestamp, image, contentLength );
	}
}
