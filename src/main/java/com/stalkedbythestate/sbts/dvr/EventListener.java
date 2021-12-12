package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(EventListener.class);
	ConcurrentHashMap<String, LinkedBlockingQueue<Request>> cameraEventQueues;
	CameraList cameraList;

	EventListener(ConcurrentHashMap<String, LinkedBlockingQueue<Request>> queues,
			CameraList cameraList) {
		this.cameraEventQueues = queues;
		this.cameraList = cameraList;
	}

	public void run() {

		logger.info(">> In EventListener thread");

		ServerSocket serverSocket = null;
		try {
			String DVR_HOSTNAME = System.getenv("DVR_HOSTNAME");
			if (DVR_HOSTNAME == null)
				DVR_HOSTNAME = System.getProperty("dvr_hostname");

			String DVR_PORT = System.getenv("DVR_PORT");
			if (DVR_PORT == null)
				DVR_PORT = System.getProperty("dvr_port");

			InetAddress address = InetAddress.getByName(DVR_HOSTNAME);
			serverSocket = new ServerSocket(Integer.parseInt(DVR_PORT), 2,
					address);
		} catch (IOException e) {
			e.printStackTrace();

			// for (String cam : Dvr.cameras) {
			for ( Camera camera : cameraList.getCameras() ) {
				LinkedBlockingQueue<Request> queue = cameraEventQueues.get(Integer.toString( camera.getIndex()));
				if (queue != null)
					queue.add(new Request(Request.QUIT, System
							.currentTimeMillis(), "Quit"));
			}
			
			return;
		}
		logger.info("Created socket");

		try {

			while (true) {
				logger.info(">> In event loop, serverSocket = "
						+ serverSocket);
				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				logger.info(">> Got an accepted socket");

				BufferedReader buf = null;
				buf = new BufferedReader(new InputStreamReader(clientSocket
						.getInputStream()));

				String request = buf.readLine();
				clientSocket.close();

				logger.info("Request = \"" + request + "\"");
				String trimmedRequest = request.trim();

				long currentTime = System.currentTimeMillis();

				if (trimmedRequest.startsWith("s ")) {
					logger.info(">> Received camera save command "
							+ trimmedRequest);
					logger.info(">> Received save command");
					String cameraString = trimmedRequest.substring(2).trim();

					Pattern p = Pattern.compile("^(\\S+)\\s+(.*)$");
					Matcher m = p.matcher(cameraString);
					String description;
					if (m.matches()) {
						logger.info("Matches");
						description = cameraString.substring(m.start(2), m
								.end(2));
						cameraString = cameraString.substring(0, m.end(1));
						logger.info("Description = \"" + description
								+ "\"");
						logger.info("CameraString now is \""
								+ cameraString + "\"");
					} else {
						logger.info("Doesn't match");
						description = cameraString;
					}

					logger.info("cameraString = \"" + cameraString
							+ "\"");

					for (String cam : cameraString.split("\\s*,\\s*")) {
						logger.info("Processing cam \"" + cam + "\"");
						logger.info("cameraEventQueues = "
								+ cameraEventQueues);
						LinkedBlockingQueue<Request> queue = cameraEventQueues
								.get(cam);
						if (queue != null)
							queue.add(new Request(Request.SAVE, currentTime,
									description));
						logger.info("Put an item on the queue");
					}
				} else if (trimmedRequest.startsWith("q")) {
					logger.info(">> Received quit command");
					for (Camera camera : cameraList.getCameras()) {
						LinkedBlockingQueue<Request> queue = cameraEventQueues
								.get(Integer.toString( camera.getIndex()));
						if (queue != null)
							queue.add(new Request(Request.QUIT, currentTime,
									"Quit"));
					}
					return;
				}
			}
		} catch (IOException e) {
			logger.info("Caught accept exception: " + e.getMessage());
			e.printStackTrace();
			for (Camera camera : cameraList.getCameras()) {
				LinkedBlockingQueue<Request> queue = cameraEventQueues.get(Integer.toString(camera.getIndex()));
				if (queue != null)
					queue.add(new Request(Request.QUIT, System
							.currentTimeMillis(), "Quit"));
			}
			return;
		}

	}
}
