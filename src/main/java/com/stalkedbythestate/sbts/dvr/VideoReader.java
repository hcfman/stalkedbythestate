package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.EventType;
import com.stalkedbythestate.sbts.eventlib.HttpAuthenticator;
import com.stalkedbythestate.sbts.eventlib.VideoTriggerEvent;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.CameraDevice;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoReader {
	private static final Logger logger = LoggerFactory.getLogger(VideoReader.class);
	private static final Logger oplogger = LoggerFactory.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;

	private final FreakApi freak;
	private final AtomicBoolean shuttingDown = new AtomicBoolean();
	private final int connectTimeout;

	static final int SAVE = 1;
	static final int QUIT = 2;

	private final CameraDevice cameraDevice;
	private final int camIndex;
	private final String cam;
	private final String urlString;
	private final CameraDevice camera;
	private final int bufferSeconds;
	private final int framesPerSecond;
	private final int continueSeconds;
	private final boolean cachingAllowed;

	private boolean isConnected = false;

	private final LinkedBlockingQueue<Event> queue;
	private final List<EventHeader> activeEventList;

	// Handle frame rates
	private final FrameRateCorrector frameRateCorrector = new FrameRateCorrector();

	public VideoReader(final CameraDevice cameraDevice, final int connectTimeout,
					   final FreakApi freak, final CameraDevice camera,
					   final LinkedBlockingQueue<Event> queue) {
		this.freak = freak;
		this.cameraDevice = cameraDevice;
		this.connectTimeout = connectTimeout;

		this.camera = camera;
		camIndex = camera.getIndex();
		cam = Integer.toString(camera.getIndex());
		urlString = camera.getUrl();
		this.queue = queue;
		activeEventList = new LinkedList<>();

		bufferSeconds = camera.getBufferSeconds();
		framesPerSecond = camera.getFramesPerSecond();
		continueSeconds = camera.getContinueSeconds();
		cachingAllowed = camera.isCachingAllowed();

		if (logger.isDebugEnabled())
			logger.debug("Cam " + camera.getIndex() + " bufferSeconds = "
					+ bufferSeconds);
		if (logger.isDebugEnabled())
			logger.debug("Cam " + camera.getIndex()
					+ " bufferFramesPerSecond = " + framesPerSecond);
		if (logger.isDebugEnabled())
			logger.debug("Cam " + camera.getIndex() + " continueSeconds = "
					+ continueSeconds);
	}

	private String boundary;

	void getBoundary(final HttpURLConnection conn) throws IOException {
		if (conn == null) {
			if (logger.isDebugEnabled())
				logger.debug("Trying to get a boundary header from a null connection");
			throw new IOException(
					"Can't find boundary specification in headers, null connection");
		}

		if (logger.isDebugEnabled())
			logger.debug("Check content type for connection: " + conn);
		final String contentType = conn.getHeaderField("Content-Type");

		if (contentType == null) {
			if (logger.isDebugEnabled())
				logger.debug("Response code: " + conn.getResponseCode());
			if (logger.isDebugEnabled())
				logger.debug("ContentType is null");
			throw new IOException(
					"Can't find boundary specification in headers, ContentType is null");
		}
		final int startPos = contentType.indexOf("boundary=");

		if (logger.isDebugEnabled())
			logger.debug("ContentType value is " + contentType);
		if (startPos < 0)
			throw new IOException(
					"Can't find boundary specification in headers, string doesn't match for url "
							+ conn.getURL() + " Found ContentType value of: "
							+ contentType);

		boundary = contentType.substring(startPos + 9).trim();
		if (logger.isDebugEnabled())
			logger.debug("Found boundary, it is " + boundary);
	}

	public void start() {
		sbtsConfig = freak.getSbtsConfig();

		final Thread videoThread = new Thread(() -> {
			if (logger.isDebugEnabled())
				logger.debug("STARTING VIDEO THREAD: " + camera.getIndex());
			while (true) {
				try {
					if (logger.isDebugEnabled())
						logger.debug("Try and read video for: "
								+ camera.getIndex());
					readVideo();
					if (logger.isDebugEnabled())
						logger.debug("Have read video for: "
								+ camera.getIndex());
				} catch (final IOException e) {
					if (isConnected)
						oplogger.info("Camera " + camIndex
								+ " disconnected");
					isConnected = false;
					cameraDevice.setUp(false);
					if (logger.isDebugEnabled())
						logger.debug("Exception caught reading video: "
								+ e.getMessage());
					expireEvents(Long.MAX_VALUE);

					// If shutting down, return from the thread
					if (shuttingDown.get()) {
						if (logger.isDebugEnabled())
							logger.debug("VIDEO READER SHUTTING DOWN, RETURNING FROM THREAD (Exception)");
						return;
					}

					try {
						Thread.sleep(30000);
					} catch (final InterruptedException e1) {
						if (logger.isDebugEnabled())
							logger.debug("Interrupted Exception", e1);
					}
					if (logger.isDebugEnabled())
						logger.debug("Try and re-establishing connection for cam: "
								+ cam);
				} catch (final URISyntaxException e) {
					if (logger.isDebugEnabled())
						logger.debug("URI syntax error reading video: "
								+ e.getMessage());
				}

				// If shutting down, return from the thread
				if (shuttingDown.get()) {
					if (logger.isDebugEnabled())
						logger.debug("VIDEO READER SHUTTING DOWN, RETURNING FROM THREAD (Exception)");
					return;
				}
			}
		});

		videoThread.setName("video-reader-" + camera.getIndex());
		videoThread.start();
	}

	void expireEvents(final long time) {
		final Iterator<EventHeader> i = activeEventList.iterator();
		while (i.hasNext()) {
			final EventHeader e = i.next();
			if (time > e.expireTime) {
				e.printWriter.println("end");
				e.getPrintWriter().close();

				i.remove();
			}
		}
	}

	String imageFilename(final String dirname, final int count) {
		final StringBuffer sb = new StringBuffer();
		final Formatter formatter = new Formatter(sb, Locale.ENGLISH);
		final String s = formatter.format(sb + "%s/%04d.jpg", dirname, count).toString();
		formatter.close();

		return s;
	}

	void readVideo() throws IOException, URISyntaxException {
		if (logger.isDebugEnabled())
			logger.debug("TRYING TO READ VIDEO, username: "
					+ camera.getUsername() + " password: "
					+ camera.getPassword());
		final HttpAuthenticator authenticator = HttpAuthenticator.getInstance();
		authenticator.setUsername(camera.getUsername());
		authenticator.setPassword(camera.getPassword());

		final URI uri = new URI(urlString);
		final URL url = uri.toURL();

		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (logger.isDebugEnabled())
			logger.debug("Connection = " + conn);
		conn.setUseCaches(cachingAllowed);
		conn.setReadTimeout(connectTimeout);
		if (logger.isDebugEnabled())
			logger.debug("Got a connection for cam: " + cam + " " + conn);

		try {
			getBoundary(conn);
		} catch (final Exception e1) {
			conn.disconnect();
			throw new IOException(e1);
		}
		if (logger.isDebugEnabled())
			logger.debug("Boundary = \"" + boundary + "\"");

		if (logger.isDebugEnabled())
			logger.debug("Authority = " + url.getAuthority());

		if (logger.isDebugEnabled())
			logger.debug("Keys are");
		final Map<String, List<String>> m = conn.getHeaderFields();
		for (final String key : m.keySet()) {
			if (logger.isDebugEnabled())
				logger.debug("key = " + key);
			for (final String value : m.get(key))
				if (logger.isDebugEnabled())
					logger.debug("Value = " + value);
			if (logger.isDebugEnabled())
				logger.debug("Value = " + m.get(key));
		}

		BufferedReader bin;
		final BufferedInputStream inStream = new BufferedInputStream(
				conn.getInputStream());
		bin = new BufferedReader(new InputStreamReader(inStream), 1);

		if (logger.isDebugEnabled())
			logger.debug("Now get images");

		final GetImage imageGetter = new GetImage();

		ImageBuffer imageBuffer;
		long dumpTillTime = 0;
		CircularList circList = new CircularList(freak, cam, framesPerSecond
				* bufferSeconds);

		final BufferDumper bufferDumper = new BufferDumper(freak);

		final LinkFile linkFile = new LinkFile();

		// Before starting, check if shutting down
		if (isShuttingDown()) {
			if (logger.isDebugEnabled())
				logger.debug("VIDEO READER SHUTTING DOWN, RETURNING FROM THREAD (Before loop)");
			return;
		}

		try {
			while ((imageBuffer = imageGetter.getImage(bin, inStream, boundary)) != null) {
				if (!frameRateCorrector.add(cam, imageBuffer.getTimestamp()))
					continue;
				final long currentTime = System.currentTimeMillis();
				if (!isConnected)
					oplogger.info("Camera " + camIndex + " connected");
				isConnected = true;
				cameraDevice.setUp(true);

				// Finished recording any camera?
				expireEvents(currentTime);

				// Any new cameras to record or finishing
				if (queue != null && !queue.isEmpty()) {
					final Event request = queue.remove();

					if (request.getEventType() == EventType.EVENT_SHUTDOWN) {
						if (logger.isDebugEnabled())
							logger.debug("Shutting down cam (" + cam + ")");
						inStream.close();
						if (isConnected)
							oplogger.info("Camera " + camIndex
									+ " disconnected");
						isConnected = false;
						cameraDevice.setUp(false);
						expireEvents(Long.MAX_VALUE);
						setShuttingDown(true);
						return;
					} else if (request.getEventType() == EventType.EVENT_CONFIGURE) {
						inStream.close();
						conn.disconnect();
						if (isConnected)
							oplogger.debug("Camera " + camIndex
									+ " disconnected");
						isConnected = false;
						cameraDevice.setUp(false);
						if (logger.isDebugEnabled())
							logger.debug("Restarting cam (" + cam + ")");
						setShuttingDown(true);
						if (logger.isDebugEnabled())
							logger.debug("VIDEO READER SHUTTING DOWN, RETURNING FROM THREAD (Queue reader)");
						return;
					} else if (request.getEventType() == EventType.VIDEO_TRIGGER) {
						if (isConnected) {
							if (logger.isDebugEnabled())
								logger.debug("Got a save request");
							if (logger.isDebugEnabled())
								logger.debug("Got a save request for cam "
										+ camIndex);
							final VideoTriggerEvent videoTriggerEvent = (VideoTriggerEvent) request;
							// Create new event
							final EventHeader event = new EventHeader(freak, cam,
									videoTriggerEvent.getEventTime(),
									videoTriggerEvent.getDescription());
							final int maxBufferSize = framesPerSecond * bufferSeconds;

							if (currentTime >= dumpTillTime) {
								if (logger.isDebugEnabled())
									logger.debug("DUMPING THE CIRCULAR BUFFER");
								circList.dump(event);
								circList = new CircularList(freak, cam,
										maxBufferSize);
							} else {
								/*
								 * Add file names from any other active event.
								 * There should always be at least one active
								 * event.
								 */
								if (!activeEventList.isEmpty()) {
									if (logger.isDebugEnabled())
										logger.debug("DUMPING ALREADY IN PROGRESS for cam "
												+ camIndex);
									// Create this in reverse, then add forward
									final LinkedList<String> reverseList = new LinkedList<>();

									final EventHeader anEvent = activeEventList
											.get(0);

									int count = 0;
									final Iterator<String> tempIt = anEvent
											.getFilelist().descendingIterator();
									// Add in reverse
									while (count < maxBufferSize
											&& tempIt.hasNext()) {
										final String fname = tempIt.next();
										reverseList.add(fname);
										count++;
									}

									// Add into real list reversed again so its
									// forward
									final Iterator<String> it = reverseList
											.descendingIterator();
									while (it.hasNext()) {
										// File borrowed from another event
										final String fname = it.next();

										final int newCount = event
												.getLastFileWritten();
										final String newFilename = imageFilename(
												event.getDirname(), newCount);

										// Link existing file to this new event
										// that overlaps, starting from 0001
										// however
										if (logger.isDebugEnabled())
											logger.debug("Linking already active ("
													+ anEvent.getEventTime()
													+ ") "
													+ fname
													+ " to "
													+ newFilename);
										linkFile.link(freak.getSbtsBase()
												+ "/disk/sbts/" + fname,
												freak.getSbtsBase()
														+ "/disk/sbts/"
														+ newFilename);

										event.getFilelist().add(newFilename);
										event.getPrintWriter().println(
												newFilename);
									}
								}
							}

							dumpTillTime = currentTime
									+ (continueSeconds * 1000L);

							event.setExpireTime(dumpTillTime);
							if (logger.isDebugEnabled())
								logger.debug("Adding event (" + event + " ["
										+ event + "])"
										+ event.getEventTime()
										+ "to the activelist for cam "
										+ camIndex);
							activeEventList.add(event);
						} // isConnected
					}

				}

				if (currentTime < dumpTillTime) {
					bufferDumper.dumpImage(cam, imageBuffer);
					final String fromFilename = freak.getSbtsBase()
							+ "/disk/sbts/tmp_images/" + cam + "/"
							+ imageBuffer.getTimestamp() + ".jpg";
					for (final EventHeader e : activeEventList) {
						final int count = e.getLastFileWritten();
						if (logger.isDebugEnabled())
							logger.debug("DUMPING CONTINUATION (event " + e
									+ " [" + e + "]) for cam "
									+ camIndex + " (" + e.getEventTime()
									+ ") FROM COUNT: " + count);
						final String fname = imageFilename(e.getDirname(), count);
						if (logger.isDebugEnabled())
							logger.debug("(" + e.getEventTime() + ")ln "
									+ freak.getSbtsBase()
									+ "/disk/sbts/tmp_images/" + cam + "/"
									+ imageBuffer.getTimestamp() + ".jpg"
									+ " to " + freak.getSbtsBase()
									+ "/disk/sbts/" + fname);
						linkFile.link(fromFilename, freak.getSbtsBase()
								+ "/disk/sbts/" + fname);

						e.getFilelist().add(fname);
						e.getPrintWriter().println(fname);
						e.getPrintWriter().flush();
					}

					// Now it's linked to all events, remove it from tmp_images
					new File(fromFilename).delete();
				} else {
					circList.add(imageBuffer);
				}
			}
		} catch (final Exception e) {
			try {
				inStream.close();
			} catch (final Exception e1) {
				logger.debug("Exception closing video stream and disconnecting");
			}
			conn.disconnect();
			throw new IOException(e);
		}
	}

	public boolean isShuttingDown() {
		return shuttingDown.get();
	}

	public void setShuttingDown(final boolean isShuttingDown) {
		shuttingDown.set(isShuttingDown);
	}

}
