package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns={"/backup"})
public class Backup extends HttpServlet {
	private static final long serialVersionUID = 4826751111201461423L;
	private static final Logger logger = Logger.getLogger(Backup.class);
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	private byte[] getBackup(HttpServletRequest request) throws Exception {
		if (freak == null)
			freak = Freak.getInstance();

		List<String> command = new ArrayList<String>();
		command.add(freak.getSbtsBase() + "/bin/backup.sh");
		ProcessBuilder pb = new ProcessBuilder(command);

		StringBuilder sb = new StringBuilder();

		synchronized (freak) {

			Process process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (logger.isDebugEnabled())
					logger.debug(line);
				sb.append(line).append("\n");
			}
			br.close();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		}

		return sb.toString().getBytes();
	}

	@RequestMapping("/backup")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		byte[] backupBytes = null;
		try {
			backupBytes = getBackup(request);
		} catch (Exception e) {
			logger.error("Error creating backup");
			return;
		}
		
		response.setContentType("application/force-download");
		response.setHeader("Content-Disposition",
				"attachment;filename=\"SBTS.bkp\"");
		response.setHeader("Accept-Ranges", "bytes");
		response.setContentLength(backupBytes.length);
		response.getOutputStream().write(backupBytes);

		logger.info("Downloaded backup");
	}
}
