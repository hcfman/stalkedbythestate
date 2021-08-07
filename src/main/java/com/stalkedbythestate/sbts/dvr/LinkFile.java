package com.stalkedbythestate.sbts.dvr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LinkFile {
	
	void link(String from, String to) {
		List<String> command = new ArrayList<String>();
		
		command.add("ln");
		command.add(from);
		command.add(to);
		ProcessBuilder pb = new ProcessBuilder(command);

		pb.redirectErrorStream(true);

		try {
			Process process = pb.start();

			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while (br.readLine() != null) {
			}
			br.close();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Close streams!
			process.getInputStream().close();
			process.getOutputStream().close();
			process.getErrorStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
