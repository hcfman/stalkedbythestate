package com.stalkedbythestate.sbts.updateavailable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();

		if (args.length != 1)
			System.exit(1);
		String version = args[0];

		main.check(version);
	}

	private void check(String version) {
		System.setProperty("javax.net.ssl.keyStore", System.getenv("HOME")
				+ "/update/" + "cacerts" + "/" + "keystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "n1txo0rdBurrN");
		System.setProperty("javax.net.ssl.trustStore", System.getenv("HOME")
				+ "/update/" + "certs" + "/" + "truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "n1txo0rdBurrN");

		URL url = null;

		String urlString = "https://update.stalkedbythestate.com:8445/check/updatesavail";
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.exit(1);
		}

		HttpsURLConnection httpsConn = null;
		URLConnection conn = null;
		InputStream in = null;
		try {
			conn = url.openConnection();

			httpsConn = (HttpsURLConnection) conn;

			conn.setUseCaches(false);
			conn.setReadTimeout(30000);

			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			try {
				httpsConn.setRequestMethod("GET");
			} catch (ProtocolException e1) {
				System.exit(1);
			}

			StringBuffer sb = new StringBuffer();
			sb.append("version=");
			try {
				sb.append(URLEncoder.encode(version, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				System.exit(1);
			}

			String outputString = sb.toString();
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(outputString.getBytes().length));
			conn.setDoOutput(true);
			DataOutputStream dataOutputStream = new DataOutputStream(
					conn.getOutputStream());
			dataOutputStream.writeBytes(outputString);
			conn.getOutputStream().close();

			// Consume and output input
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			OutputStream out = System.out;
			while ((bytesRead = conn.getInputStream().read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}

		} catch (IOException e) {
			System.exit(1);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}

		System.exit(0);
	}

}
