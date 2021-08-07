package com.stalkedbythestate.sbts.dvr;

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class EventHeader {
	static final Logger logger = Logger.getLogger(EventHeader.class);
	private FreakApi freak;
	
	private AtomicInteger lastFileWrittenAtomic = new AtomicInteger(0);
	long eventTime;
	long expireTime;
	private final String description;
	private final String dirname;
	PrintWriter printWriter;
	
	String cam;
	LinkedList<String> filelist = new LinkedList<String>();

	EventHeader(FreakApi freak, String cam, long eventTime, String description) throws IOException {
		this.freak = freak;
		this.description = description;
		this.eventTime = eventTime;
		this.cam = cam;
		
		if (logger.isDebugEnabled()) logger.debug("EVENT HEADER: " + freak.getSbtsBase());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(eventTime);
		String dateString = dateFormat.format(d);

		String eventDirname = freak.getSbtsBase() + "/disk/sbts/events/" + cam + "/" + dateString;
		File eventsDir = new File(eventDirname);
		eventsDir.mkdirs();
		
		File file = new File( eventDirname + "/" + eventTime );
		printWriter = new PrintWriter( new FileOutputStream( file ));
		printWriter.println( description );
		logger.debug("Created print writer on " + file.toString() );

		dirname = "dvr_images/" + cam + "/" + dateString + "/" + eventTime;
		File dirFile = new File(freak.getSbtsBase() + "/disk/sbts/" + dirname);
		dirFile.mkdirs();
	}

	void setExpireTime(long time) {
		expireTime = time;
	}
	
	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	PrintWriter getPrintWriter() {
		return printWriter;
	}

	public LinkedList<String> getFilelist() {
		return filelist;
	}

	public String getDescription() {
		return description;
	}

	public int getLastFileWritten() {
		return lastFileWrittenAtomic.addAndGet(1);
	}

	public String getDirname() {
		return dirname;
	}

	@Override
	public String toString() {
		return "EventHeader [cam=" + cam + ", description=" + description
				+ ", dirname=" + dirname + ", eventTime=" + eventTime
				+ ", expireTime=" + expireTime + ", lastFileWrittenAtomic="
				+ lastFileWrittenAtomic + "]";
	}

}
