package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns={"/cam"})
public class Streamer extends HttpServlet {
	private static final long serialVersionUID = -3975176759451571602L;
	private static final Logger logger = LoggerFactory.getLogger(Streamer.class);
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		// System.out.println("In service");
		// getSelected();
		if (logger.isDebugEnabled())
			logger.debug("In /cam, streaming");

		String cam = request.getParameter("cam");
		String event = request.getParameter("t");

		String offsetString = request.getParameter("offset");
		int offsetValue = 0;
		String lengthString = request.getParameter("length");
		int lengthValue = 0;

		if (offsetString != null) {
			try {
				offsetValue = Integer.parseInt(offsetString);
				offsetValue *= 5;
			} catch (NumberFormatException e) {
				throw new ServletException("Invalid offset value ("
						+ offsetString + "), expecting an integer");
			}
		}
		int skipCount = offsetValue * 5;

		if (lengthString != null) {
			try {
				lengthValue = Integer.parseInt(lengthString);
			} catch (NumberFormatException e) {
				throw new ServletException("Invalid length value ("
						+ offsetString + "), expecting an integer");
			}
		}

		boolean bounded = lengthString != null;
		boolean looping = offsetString != null || lengthString != null;

		int maxFrameNumber = (offsetValue + lengthValue) * 5;

		OutputStream outStream = response.getOutputStream();
		response.setContentType("multipart/x-mixed-replace;boundary=--myboundary");
		response.setHeader("Connection", "close");

		long eventTime = 0L;
		try {
			eventTime = Long.parseLong(event);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}

		if (logger.isDebugEnabled())
			logger.debug("eventTime: " + eventTime);
		Date d = new Date(eventTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dayString = sdf.format(d).toString();
		if (logger.isDebugEnabled())
			logger.debug("DayString: " + dayString);

		String filename = freak.getSbtsBase() + "/disk/sbts/events/" + cam + "/"
				+ dayString + "/" + event;
		File infile = new File(filename);
		if (!infile.exists()) {
			return;
		}

		try {
			BufferedReader inStream = new BufferedReader(new InputStreamReader(
					new FileInputStream(infile)));
			// Skip the description line
			String line = inStream.readLine();
			if (looping)
				inStream.mark(200000);
			int count = 0;
			while ((line = inStream.readLine()) != null && !line.equals("")) {
				if (line.equals("end"))
					if (looping) {
						inStream.reset();
						count = 0;
						skipCount = offsetValue * 5;
						continue;
					} else
						break;

				count++;
				if (looping && skipCount-- > 0)
					continue;

				String imageFilename = freak.getSbtsBase() + "/disk/sbts/" + line;
				File imageFile = new File(imageFilename);
				FileInputStream imageStream = new FileInputStream(imageFile);

				outStream.write("--myboundary\r\n".getBytes());
				outStream.write("Content-Type: image/jpeg\r\n".getBytes());

				outStream
						.write(("Content-Length: " + imageFile.length() + "\r\n\r\n")
								.getBytes());

				byte buffer[] = new byte[(int) imageFile.length()];
				int offset = 0, bytesRead = 0, totalSize = (int) imageFile
						.length();
				while (offset != totalSize
						&& (bytesRead = imageStream.read(buffer, offset,
								totalSize - offset)) > 0)
					offset += bytesRead;

				outStream.write(buffer);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (totalSize > 2 && ((char) buffer[totalSize - 2]) != '\r'
						&& ((char) buffer[totalSize - 1]) != '\n')

					outStream.write("\r\n".getBytes());
				imageStream.close();

				if (bounded && count == maxFrameNumber) {
					inStream.reset();
					count = 0;
					skipCount = offsetValue * 5;
				}
			}
		} catch (Exception e) {
			// Catch exceptions. Potentially ok to simply get an I/O Exception
		}

	}

}
