package com.stalkedbythestate.sbts.rfxcomhandler;

// Copyright (c) 2021 Kim Hendrikse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class HandlePackets implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HandlePackets.class);
	private FileInputStream fis;
	private FileOutputStream fos;
	private RfxcomController rfxcomController;

	public HandlePackets(RfxcomController rfxcomController, FileInputStream fis, FileOutputStream fos) {
		this.rfxcomController = rfxcomController;
		this.fis = fis;
		this.fos = fos;
	}

	@Override
	public void run() {
		try {
			rfxcomController.start(fis, fos);
		} catch (FileNotFoundException e) {
			logger.error("File not found Exception starting RFXCOM controller", e);
		}
	}

}
