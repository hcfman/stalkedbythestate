package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileBlob {
	private static final Logger logger = LoggerFactory.getLogger(FileBlob.class);
	private static final int BUFFERSIZE = 1024;
	
	public String getBlob(String filename) throws Exception {
		File inputFile = new File(filename);
		char[] buffer = new char[BUFFERSIZE];
		StringBuffer sb = new StringBuffer();
		FileReader freader;
		try {
			freader = new FileReader(inputFile);
			int bytesRead;
			while ((bytesRead = freader.read(buffer, 0, BUFFERSIZE)) > 0) {
				sb.append(buffer, 0, bytesRead);
			}
			freader.close();
		} catch (FileNotFoundException e) {
			if (logger.isDebugEnabled()) logger.debug("File not found (" + filename + ")");
			throw new Exception("File not found");
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.debug("I/O error reading (" + filename + ")");
			throw new Exception("I/O error");
		}
		
		
		return sb.toString();
	}
}
