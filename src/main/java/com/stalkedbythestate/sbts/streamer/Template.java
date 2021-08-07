package com.stalkedbythestate.sbts.streamer;

import java.io.*;

public class Template {

	static String getTemplate(String filename ) throws FileNotFoundException, IOException {
		File infile = new File( filename );
		if (!infile.exists())
			throw new FileNotFoundException(
					"Can't open template " + filename );

		BufferedReader inStream = new BufferedReader(new InputStreamReader(
				new FileInputStream(infile)));

		char buffer[] = new char[(int) infile.length()];

		int amount = inStream.read(buffer, 0, (int) infile.length());

		inStream.close();

		return new String(buffer);
	}

}
