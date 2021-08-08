package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Locale;

public class CircularList {
	private static final Logger logger = Logger.getLogger(CircularList.class);
	private FreakApi freak;
	private ImageBuffer list[];
	int size, itemCount;
	int head, tail;
	boolean full = false;
	private String cam;

	CircularList(FreakApi freak) {
		this.freak = freak;
	}

	CircularList(FreakApi freak, String cam, int size) {
		this.freak = freak;
		this.size = size;
		this.list = new ImageBuffer[ size ];
		head = tail = itemCount = 0;
		this.cam = cam;
	}

	boolean isEmpty() {
		return !full && head == tail;
	}

	boolean isFull() {
		return full;
	}

	void add(ImageBuffer item) {
		if (full) {
			get();
		} else {
			itemCount++;
		}

		list[ head++ ] = item;
		if (head == size)
			head = 0;

		if (head == tail)
			full = true;
	}

	ImageBuffer get() {
		if (isEmpty())
			return null;
		itemCount--;

		ImageBuffer item = list[ tail++];

		if (tail == size)
			tail = 0;

		full = false;

		return item;
	}
	
	int n() {
		return itemCount;
	}

	String imageFilename(String dirname, int count) {
		StringBuffer sb = new StringBuffer();
		Formatter formatter = new Formatter(sb, Locale.ENGLISH);
		String s = formatter.format(sb + "%s/%04d.jpg", dirname, count).toString();
		formatter.close();
		
		return s;
	}
	
	void dump(final EventHeader event) {
		LinkedList<String> filelist = event.getFilelist();
		
		if (!isEmpty()) {
//			if (logger.isDebugEnabled()) logger.debug( "In dump, linked list is not empty, size is " + size );
			int t = tail;
//			if (logger.isDebugEnabled()) logger.debug( "Cam " + cam + " t = " + t + ", head = " + head );
			
			PrintWriter printWriter = event.getPrintWriter();
			do  {
//				if (logger.isDebugEnabled()) logger.debug( "Cam " + cam + " t = " + t );
				ImageBuffer item = list[ t++];
				int count = event.getLastFileWritten();
				String filename = imageFilename(event.getDirname(), count);
				filelist.add(filename);
//				if (logger.isDebugEnabled()) logger.debug( "In dump (" + event.getEventTime() + "), outputting " + filename );
				printWriter.println(filename);
				printWriter.flush();

				if (t == size)
					t = 0;
			} while ( t != head );
		} else {
//			if (logger.isDebugEnabled()) logger.debug( "Linked List is empty" );
		}
		
//		if (logger.isDebugEnabled()) logger.debug( "Added the filenames to the list, going to dump files in the background" );

		// Dump the buffer in the background
		new Thread(new Runnable() {
			public void run() {
				BufferDumper bufferDumper = new BufferDumper(freak);

				LinkFile linkFile = new LinkFile();
				int count = 1;
				while (!isEmpty()) {
//					if (logger.isDebugEnabled()) logger.debug("Dumping circlist");
					ImageBuffer imageBuffer = get();
					bufferDumper.dumpImage(cam, imageBuffer);
					if (logger.isDebugEnabled()) logger.debug("DUMPING BUFFER (" + event.getEventTime() + ") WITH COUNT: " + count);
					String fromFileName = freak.getSbtsBase() + "/disk/sbts/tmp_images/" + cam + "/" + imageBuffer.getTimestamp() + ".jpg";
					if (logger.isDebugEnabled()) logger.debug("Linking (" + event.getEventTime() + ") " + fromFileName + " to " +
							freak.getSbtsBase() + "/" + imageFilename(event.getDirname(), count));
					linkFile.link(fromFileName, freak.getSbtsBase() + "/disk/sbts/" + imageFilename(event.getDirname(), count));
					// Now unlink it from the temp area
					new File(fromFileName).delete();
					count++;
				}

			}
		}).start();
	}

}
