package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns={"/restore"})
public class Restore extends HttpServlet {
	private static final long serialVersionUID = 1126998332711645868L;
	private static final Logger logger = Logger.getLogger(Restore.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
		freak = Freak.getInstance();
	}

	private void restoreBytes(byte[] configBytes) {
		if (freak == null)
			freak = Freak.getInstance();

		List<String> command = new ArrayList<String>();
		command.add(freak.getSbtsBase() + "/bin/restore.sh");
		ProcessBuilder pb = new ProcessBuilder(command);

		Process process = null;
		try {
			process = pb.start();

			OutputStream os = process.getOutputStream();
			os.write(configBytes);
			os.flush();

			try {
				process.getOutputStream().close();
				process.waitFor();
			} catch (InterruptedException e) {
				logger.error("Can't restore configuration, error writing to restore");
				return;
			}

			process.getErrorStream().close();
			process.getInputStream().close();
		} catch (IOException e) {
			logger.error("Failed to save config: " + e.getMessage());
		}

		if (process.exitValue() == 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							logger.error("Failed to delay for 2 seconds before restarting");
						}
						Restarter.restart(freak);
					} catch (IOException e) {
						logger.error("Failed to restart application after restoring configuration, please manually restart");
					}
				}
			}).start();
		}
	}

	@RequestMapping("/restore")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		synchronized (freak) {

			int contentLength = request.getContentLength();
			if (contentLength <= 0 || contentLength > 2 * 1024 * 1024)
				return;

			byte[] result = new byte[contentLength];

			ServletFileUpload fileUpload = new ServletFileUpload();

			try {
				List<FileItem> items = new ServletFileUpload(
						new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {
						// Process regular form field (input
						// type="text|radio|checkbox|etc", select, etc).
						String fieldname = item.getFieldName();
						String fieldvalue = item.getString();
						// ... (do your job here)
					} else {
						// Process form file field (input type="file").
						String fieldname = item.getFieldName();
						// String filename =
						// FilenameUtils.getName(item.getName());
						InputStream filecontent = item.getInputStream();
						InputStreamReader isr = new InputStreamReader(
								filecontent);

						BufferedReader br = new BufferedReader(isr);
						String line;
						StringBuilder sb = new StringBuilder();
						while ((line = br.readLine()) != null) {
							if (logger.isDebugEnabled())
								logger.debug(line);
							sb.append(line).append("\n");
						}
						br.close();

						try {
							freak.mountReadWrite();
							opLogger.info("Restoring configuration");
							restoreBytes(sb.toString().getBytes());
						} finally {
							freak.mountReadonly();
						}
					}
				}
			} catch (FileUploadException e) {
				throw new ServletException("Cannot parse mulij reindex" +
                        "tipart request.", e);
			}
		}

		RequestDispatcher view = request
				.getRequestDispatcher("jsp/Restore.jsp");
		view.forward(request, response);
	}
}
