package com.stalkedbythestate.sbts.dvr;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferDumper {
	private FreakApi freak;
	static final Logger logger = LoggerFactory.getLogger(BufferDumper.class);

	public BufferDumper(FreakApi freak) {
		this.freak = freak;
	}

	void dumpImage(String cam, ImageBuffer imageBuffer) {
		try {
			String dir = "tmp_images/" + cam + "/";
			String filename = imageBuffer.getTimestamp() + ".jpg";

			File dirFile = new File(freak.getSbtsBase() + "/disk/sbts/" + dir);
			dirFile.mkdirs();
			File file = new File(freak.getSbtsBase() + "/disk/sbts/" + dir
					+ filename);

			if (logger.isDebugEnabled())
				logger.debug("Filename \"" + filename + "\"");
			FileOutputStream outStream = new FileOutputStream(file);
			logger.debug("Writing " + imageBuffer.size());
			outStream.write(imageBuffer.getBuffer(), 0, imageBuffer.size());
			outStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
