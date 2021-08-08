package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stalkedbythestate.sbts.freak.Freak;
import com.stalkedbythestate.sbts.freakutils.ScriptRunner;
import com.stalkedbythestate.sbts.freakutils.ScriptRunnerResult;
import com.stalkedbythestate.sbts.json.NetworkJSON;
import com.stalkedbythestate.sbts.json.ResultMessage;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns={"/network"})
public class Network extends HttpServlet {
	private static final long serialVersionUID = -1677036780021357071L;
	private static final Logger logger = Logger.getLogger(Network.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private SbtsDeviceConfig sbtsConfig;
	private FreakApi freak;

	@Override
	public void init() throws ServletException {
//		freak = Freak.getInstance();
	}

	public void validateIP(String fieldname, String addressString)
			throws Exception {
		if (addressString.equals("")) {
			if (logger.isDebugEnabled())
				logger.debug("Blank");
			throw new Exception(fieldname + " cannot be empty");
		}

		String[] parts = addressString.split("\\.");

		if (!addressString
				.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
			if (logger.isDebugEnabled())
				logger.debug("No matches");
			throw new Exception(fieldname
					+ " must comprise 4 numbers separated by dots");
		}

		for (String s : parts) {
			int i = Integer.parseInt(s);
			if (i < 0 || i > 255) {
				if (logger.isDebugEnabled())
					logger.debug("Range");
				throw new Exception("Out of range numbers found in "
						+ fieldname);
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("Returning");

		return;
	}

	private void changeNetworkSettings(NetworkJSON networkJSON) {
	}

	@RequestMapping("/network")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (freak == null)
			freak = Freak.getInstance();

		response.setContentType("text/html");

		sbtsConfig = freak.getSbtsConfig();
		if (logger.isDebugEnabled())
			logger.debug("sbtsConfig: " + sbtsConfig);

		if (request.getMethod().equals("POST")) {
			if (logger.isDebugEnabled())
				logger.debug("Posting network");
			response.setContentType("application/json");

			if (logger.isDebugEnabled())
				logger.debug("content-type: "
						+ request.getHeader("content-type"));

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

			ResultMessage resultMessage = new ResultMessage(true, "");
			PrintWriter out = response.getWriter();

			Gson fromGson = new Gson();
			NetworkJSON networkJSON = fromGson
					.fromJson(body, NetworkJSON.class);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			if (logger.isDebugEnabled())
				logger.debug("networkJSON has become: "
						+ gson.toJson(networkJSON));

			// Hostname
			String hostname = null;
			String address = null;
			if ((hostname = networkJSON.getHostname()) == null
					|| (hostname = hostname.trim()).equals("")) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add("hostname must not be blank");
			} else if (!hostname
					.matches("^(?:(?:[a-zA-Z\\-0-9]*[a-zA-Z0-9])\\.?)+$")) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"Invalid characters found in the hostname");
			}
			if (logger.isDebugEnabled())
				logger.debug("Hostname is: " + hostname);

			// Network settings
			if (!networkJSON.isDhcp()) {
				try {
					validateIP("address", networkJSON.getAddress());
				} catch (Exception e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception: " + e.getMessage());
					resultMessage.setResult(false);
					resultMessage.getMessages().add(e.getMessage());
				}

				try {
					validateIP("mask", networkJSON.getMask());
				} catch (Exception e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception: " + e.getMessage());
					resultMessage.setResult(false);
					resultMessage.getMessages().add(e.getMessage());
				}

				try {
					validateIP("router", networkJSON.getDefaultRoute());
				} catch (Exception e) {
					if (logger.isDebugEnabled())
						logger.debug("Exception: " + e.getMessage());
					resultMessage.setResult(false);
					resultMessage.getMessages().add(e.getMessage());
				}

				if (networkJSON.getNameServer1() != null
						&& !networkJSON.getNameServer1().trim().equals("")) {
					try {
						validateIP("Nameserver #1",
								networkJSON.getNameServer1());
					} catch (Exception e) {
						if (logger.isDebugEnabled())
							logger.debug("Exception: " + e.getMessage());
						resultMessage.setResult(false);
						resultMessage.getMessages().add(e.getMessage());
					}
				}

				if (networkJSON.getNameServer2() != null
						&& !networkJSON.getNameServer2().trim().equals("")) {
					try {
						validateIP("Nameserver #2",
								networkJSON.getNameServer2());
					} catch (Exception e) {
						if (logger.isDebugEnabled())
							logger.debug("Exception: " + e.getMessage());
						resultMessage.setResult(false);
						resultMessage.getMessages().add(e.getMessage());
					}
				}

				if (networkJSON.getNameServer3() != null
						&& !networkJSON.getNameServer3().trim().equals("")) {
					try {
						validateIP("Nameserver #3",
								networkJSON.getNameServer3());
					} catch (Exception e) {
						if (logger.isDebugEnabled())
							logger.debug("Exception: " + e.getMessage());
						resultMessage.setResult(false);
						resultMessage.getMessages().add(e.getMessage());
					}
				}

			}

			// Don't update when updating
			if (freak.getUpdating().get()) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"Can't update settings, update is in progress");
			}

			if (!resultMessage.isResult()) {
				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

			int httpPort = networkJSON.getHttpPort();
			int httpsPort = networkJSON.getHttpsPort();

			if (httpPort == httpsPort) {
				resultMessage.setResult(false);
				resultMessage.getMessages().add(
						"HTTP port should be different from the HTTPS port");
			} else if (httpPort < 1024 || httpsPort < 1024 || httpPort > 32000
					|| httpsPort > 32000 || httpPort == 16001
					|| httpsPort == 16001) {
				resultMessage.setResult(false);
				resultMessage
						.getMessages()
						.add("Invalid port chosen. Ports should be between 1024 and 32000");
			}

			if (!resultMessage.isResult()) {
				out.print(gson.toJson(resultMessage));
				out.close();
				return;
			}

/*
			synchronized (freak) {
				try {
					freak.mountReadWrite();
					opLogger.info("Saving network settings");

					TomcatSettings tomcatSettings = new TomcatSettings();
					boolean updated = false;
					try {
						if (logger.isDebugEnabled())
							logger.debug("keystorepassword: "
									+ sbtsConfig.getSettingsConfig()
											.getKeystorePassword());
						tomcatSettings.updateTomcatSettings(networkJSON,
								sbtsConfig.getSettingsConfig()
										.getKeystorePassword());
						updated = true;
					} catch (Exception e) {
						opLogger.error("Can't update the web settings: "
								+ e.getMessage());
						resultMessage = new ResultMessage(false,
								"Can't update the web settings: "
										+ e.getMessage());

						out.print(gson.toJson(resultMessage));
						out.close();
						return;
					}
					opLogger.info("Updated webserver ports and protocols");

					// Change tomcat server.xml
					String catalinaHome;

					catalinaHome = System.getenv("CATALINA_HOME");

					File serverFile = new File(catalinaHome
							+ "/conf/server.xml");
					File newServerFile = new File(catalinaHome
							+ "/conf/newserver.xml");

					out.print(gson.toJson(resultMessage));
					out.close();

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}

					// Update network settings for real

					if (updated && newServerFile.exists()) {
						serverFile.delete();
						newServerFile.renameTo(serverFile);
						// Restarter.restart();
						changeNetworkSettings(networkJSON);
					}
				} finally {
					freak.mountReadonly();
				}
			}
*/

		} else {
			RequestDispatcher view = request
					.getRequestDispatcher("/jsp/content/components/ErrorPage.jsp");
			view.forward(request, response);
		}
	}

}
