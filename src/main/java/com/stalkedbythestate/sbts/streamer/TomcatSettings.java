package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import com.stalkedbythestate.sbts.json.NetworkJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class TomcatSettings {
	private static final Logger logger = LoggerFactory.getLogger(TomcatSettings.class);
	private static final Logger opLogger = LoggerFactory.getLogger("operations");

	public NetworkJSON getSettings() throws Exception {
		NetworkJSON networkJSON = new NetworkJSON();
		
		String catalinaHome;

		catalinaHome = System.getenv("CATALINA_HOME");

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		InputSource source = new InputSource(catalinaHome
				+ "/conf/server.xml");
		NodeList connectors = (NodeList) xpath.evaluate("/Server/Service[@name=\"Catalina\"]/Connector",
				source, XPathConstants.NODESET);
		
		int nonSSLport = 8080;
		int sslPort = 8443;
		boolean hasNonSSL = false;
		boolean hasSSL = false;
		for (int i = 0; i < connectors.getLength(); i++) {
			Element e = (Element) connectors.item(i);
			
			String ssLEnabledString = e.getAttribute("SSLEnabled");
			boolean isSSL = "true".equals(ssLEnabledString);
			
			String portString = e.getAttribute("port");
			int port = 8080;
			try {
				port = Integer.parseInt(portString);
			} catch (Exception e1) {
				logger.error("Can't parse port from \"" + portString + "\"");
			}
			
			if (isSSL) {
				hasSSL = true;
				sslPort = port;
				if (logger.isDebugEnabled()) logger.debug("Has SSL");
				if (logger.isDebugEnabled()) logger.debug("non-SSL port is: " + port);
			} else {
				hasNonSSL = true;
				nonSSLport = port;
				if (logger.isDebugEnabled()) logger.debug("Has non-SSL");
				if (logger.isDebugEnabled()) logger.debug("non-SSL port is: " + port);
			}
		}
		
		if (!hasSSL && !hasNonSSL)
			throw new Exception("You must define at least one of SSL or non-SSL configurations");
		
		networkJSON.setHttpPort(nonSSLport);
		networkJSON.setHttpsPort(sslPort);
		
		if (hasSSL && hasNonSSL) {
			networkJSON.setProtocolDescriptor("HTTP-HTTPS");
		} else {
			if (hasSSL)
				networkJSON.setProtocolDescriptor("HTTPS");
			else
				networkJSON.setProtocolDescriptor("HTTP");
		}

		return networkJSON;
	}
	
	public void updateTomcatSettings(NetworkJSON tomcatNetworkJSON, String keystorePassword) throws Exception {
		String catalinaHome;

		catalinaHome = System.getenv("CATALINA_HOME");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			InputSource source = new InputSource(catalinaHome + "/conf/server.xml");
			Document doc = docBuilder.parse(source);
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			NodeList connectors = (NodeList) xpath.evaluate("/Server/Service[@name=\"Catalina\"]/Connector",
					doc, XPathConstants.NODESET);
			
			for (int i = 0; i < connectors.getLength(); i++) {
				Node node = connectors.item(i);
				node.getParentNode().removeChild(node);
			}
			
			Node parentNode = ((NodeList) xpath.evaluate("/Server/Service[@name=\"Catalina\"]",
					doc, XPathConstants.NODESET)).item(0);
			
			String protocolDescriptor = tomcatNetworkJSON.getProtocolDescriptor();
			
			if ("HTTP".equals(protocolDescriptor)) {
				Element e = doc.createElement("Connector");
				e.setAttribute("port", Integer.toString(tomcatNetworkJSON.getHttpPort()));
				e.setAttribute("protocol", "org.apache.coyote.http11.Http11NioProtocol");
				e.setAttribute("connectionTimeout", "20000");
				e.setAttribute("redirectPort", Integer.toString(tomcatNetworkJSON.getHttpsPort()));
				parentNode.appendChild(e);
			} else if ("HTTPS".equals(protocolDescriptor)) {
				Element e = doc.createElement("Connector");
				e.setAttribute("port", Integer.toString(tomcatNetworkJSON.getHttpsPort()));
				e.setAttribute("protocol", "HTTP/1.1");
				e.setAttribute("SSLEnabled", "true");
				e.setAttribute("maxThreads", "150");
				e.setAttribute("scheme", "https");
				e.setAttribute("secure", "true");
				e.setAttribute("keystoreFile", "${user.home}/app/cacerts/keystore.jks");
				e.setAttribute("keystorePass", keystorePassword);
				e.setAttribute("keyAlias", "sbts");
				e.setAttribute("clientAuth", "false");
				e.setAttribute("sslProtocol", "TLS");
				parentNode.appendChild(e);
			} else {
				Element e1 = doc.createElement("Connector");
				e1.setAttribute("port", Integer.toString(tomcatNetworkJSON.getHttpPort()));
				e1.setAttribute("protocol", "HTTP/1.1");
				e1.setAttribute("connectionTimeout", "20000");
				e1.setAttribute("redirectPort", Integer.toString(tomcatNetworkJSON.getHttpsPort()));
				parentNode.appendChild(e1);
				
				Element e2 = doc.createElement("Connector");
				e2.setAttribute("port", Integer.toString(tomcatNetworkJSON.getHttpsPort()));
				e2.setAttribute("protocol", "HTTP/1.1");
				e2.setAttribute("SSLEnabled", "true");
				e2.setAttribute("maxThreads", "150");
				e2.setAttribute("scheme", "https");
				e2.setAttribute("secure", "true");
				e2.setAttribute("keystoreFile", "${user.home}/app/cacerts/keystore.jks");
				e2.setAttribute("keystorePass", keystorePassword);
				e2.setAttribute("keyAlias", "sbts");
				e2.setAttribute("clientAuth", "false");
				e2.setAttribute("sslProtocol", "TLS");
				parentNode.appendChild(e2);
			}
		    
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(new DOMSource(doc),
		    		new StreamResult(new File(catalinaHome + "/conf/newserver.xml")));
		    opLogger.info("Updated users");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		} catch (TransformerException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the web settings: " + e.getMessage());
			throw new Exception("Problem updating the web settings: " + e.getMessage());
		}
		
	}
}
