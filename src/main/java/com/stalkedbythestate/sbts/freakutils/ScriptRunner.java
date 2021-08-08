package com.stalkedbythestate.sbts.freakutils;

// Copyright (c) 2021 Kim Hendrikse

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ScriptRunner {
	private static final Logger logger = Logger.getLogger(ScriptRunner.class);

	public ScriptRunnerResult spawn(String... command) {
		if (logger.isDebugEnabled())
			logger.debug("Spawning ScriptRunner with: "
					+ Arrays.toString(command));
		ScriptRunnerResult scriptRunnerResult = new ScriptRunnerResult();
		scriptRunnerResult.setResult(-1);

		ProcessBuilder pb = new ProcessBuilder(command);

		Process process = null;
		try {
			process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[1024];
			int amountRead = 0;
			while ((amountRead = br.read(buffer)) > 0) {
				sb.append(buffer, 0, amountRead);
				if (logger.isDebugEnabled())
					logger.debug("Appending data");
			}
			br.close();
			if (logger.isDebugEnabled())
				logger.debug("Closing stream");

			try {
				if (logger.isDebugEnabled())
					logger.debug("Wait for process");
				int result = process.waitFor();
				if (logger.isDebugEnabled())
					logger.debug("Waited");

				scriptRunnerResult.setResult(result);
				scriptRunnerResult.setOutput(sb.toString());
			} catch (InterruptedException e) {
				e.printStackTrace();
				// Returns -1
			}

			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return scriptRunnerResult;
	}

}
