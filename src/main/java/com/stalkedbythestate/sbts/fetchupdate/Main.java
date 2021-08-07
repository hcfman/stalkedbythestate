package com.stalkedbythestate.sbts.fetchupdate;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();

		if (args.length != 2)
			System.exit(1);
		String version = args[0];
		
		int count = 0;
		try {
			count = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.exit(1);
		}
		main.fetch(version, count);
	}

	private void fetch(String version, int count) {
		System.setProperty("javax.net.ssl.keyStore", System.getenv("HOME")
				+ "/update/" + "cacerts" + "/" + "keystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "n1txo0rdBurrN");
		System.setProperty("javax.net.ssl.trustStore", System.getenv("HOME")
				+ "/update/" + "certs" + "/" + "truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "n1txo0rdBurrN");

		URL url = null;

		String urlString = "https://update.stalkedbythestate.com:8445/update/" + version + "/update."
				+ count + ".up";
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

			try {
				httpsConn.setRequestMethod("GET");
			} catch (ProtocolException e1) {
				System.exit(1);
			}

			// Consume and toss input
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
