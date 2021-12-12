package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.json.ImportCertJSON;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/importowncert"})
public class ImportOwnCert extends HttpServlet {
	private static final long serialVersionUID = 8861691397300071567L;
	private static final Logger logger = LoggerFactory.getLogger(ImportOwnCert.class);
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	private ImportCertJSON importIt(ImportCertJSON importCertJSON)
			throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("In importIt");
		CertInstaller certInstaller = new CertInstaller(freak, sbtsConfig,
				freak.getSbtsBase() + "/tmp");
		if (logger.isDebugEnabled())
			logger.debug("Call importCert");
		certInstaller.importCert(true, "sbts", importCertJSON.getContent());

		importCertJSON.setResult(true);

		return importCertJSON;
	}

	@RequestMapping("/importowncert")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("application/json");

		sbtsConfig = freak.getSbtsConfig();

		response.setContentType("application/json");

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		String body = stringBuilder.toString();

		if (logger.isDebugEnabled())
			logger.debug("Body: " + body);

		Gson fromGson = new Gson();
		ImportCertJSON importCertJSON = fromGson.fromJson(body,
				ImportCertJSON.class);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (logger.isDebugEnabled())
			logger.debug("actionsJSON has become: "
					+ gson.toJson(importCertJSON));

		PrintWriter out = response.getWriter();

		try {
			if (logger.isDebugEnabled())
				logger.debug("Calling importCertJSON");
			importCertJSON = importIt(importCertJSON);
		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("Caught exception from importIt: "
						+ e.getMessage());
			e.printStackTrace();
			importCertJSON.getMessages().add(
					"Cannot import certificate: " + e.getMessage());
		}

		importCertJSON.setAlias(null);
		importCertJSON.setContent(null);

		out.print(gson.toJson(importCertJSON));
	}
}
