package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns={"/img"})
public class Img extends HttpServlet {
	private static final long serialVersionUID = 5019870886557760386L;
	private static final Logger logger = Logger.getLogger(Img.class);
	SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}
	
	private void abort(final HttpServletResponse response, final String message) throws ServletException, IOException {
		response.setContentType("text/plain");
		final PrintWriter out = response.getWriter();
		
		out.println(message);
		out.close();
	}

	@RequestMapping("/img")
	protected void service(final HttpServletRequest request,
						   final HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		if (!freak.isReady())
			return;

		final String t = request.getParameter("t");
		final String n = request.getParameter("n");
		final String cam = request.getParameter("cam");
		
		if (logger.isDebugEnabled()) logger.debug("Cam: " + cam);
		if (t == null) {
			abort(response, "Parameter t is missing");
			return;
		}
		if (!t.matches("^\\d+$")) {
			abort(response, "Parameter t should be a number");
			return;
		}
		final long tValue = Long.parseLong(t);

		if (cam == null) {
			abort(response, "Parameter cam is missing");
			return;
		}
		if (!cam.matches("^\\d+$")) {
			abort(response, "Parameter cam should be a number");
			return;
		}
		
		if (n == null) {
			abort(response, "Parameter n is missing");
			return;
		}
		if (!n.matches("^\\d+$")) {
			abort(response, "Parameter n should be a number");
			return;
		}
		
		final int nValue = Integer.parseInt(n);
		
		if (nValue < 1 || nValue > 9999) {
			abort(response, "Parameter n should be >= 1 & <= 9999");
			return;
		}
		
		final Date d = new Date(tValue);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String dayString = sdf.format(d);
		
		final String filename = freak.getSbtsBase() + "/disk/sbts/images/" + cam + "/" + dayString + "/" + t + "/" + String.format("%04d", nValue) + ".jpg";
		if (logger.isDebugEnabled()) logger.debug("Filename: " + filename);
		final File infile = new File(filename);
		if (!infile.exists()) {
			abort(response, "Not found");
			return;
		}
		
		response.setContentType("image/jpeg");

		final File imageFile = new File(filename);
		response.setContentLength((int) imageFile.length());
		final FileInputStream imageStream = new FileInputStream(imageFile);

		final byte[] buffer = new byte[(int) imageFile.length()];
		int offset = 0;
		int bytesRead = 0;
		final int totalSize = (int) imageFile.length();
		while (offset != totalSize
				&& (bytesRead = imageStream.read(buffer, offset, totalSize
						- offset)) > 0)
			offset += bytesRead;

		final OutputStream outStream = response.getOutputStream();

		outStream.write(buffer);
		try {
			Thread.sleep(200);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		outStream.write("\r\n".getBytes());
		imageStream.close();

		outStream.close();
	}

}
