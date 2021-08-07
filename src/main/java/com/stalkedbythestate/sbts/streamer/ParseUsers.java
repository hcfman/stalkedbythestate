package com.stalkedbythestate.sbts.streamer;

import com.stalkedbythestate.sbts.json.UserJSON;
import com.stalkedbythestate.sbts.json.UsersJSON;
import org.apache.log4j.Logger;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParseUsers {
	private static final Logger logger = Logger.getLogger(ParseUsers.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	
	public UsersJSON parse() throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
		UsersJSON usersJSON = new UsersJSON(true);
		String catalinaHome;

		catalinaHome = System.getenv("CATALINA_HOME");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(new FileInputStream(catalinaHome + "/conf/" + "tomcat-users.xml"));

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		xPath.setNamespaceContext(new NamespaceResolver(doc));

		NodeList nodeList = (NodeList) xPath.evaluate("/tomcat-users/user", doc, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element e = (Element) nodeList.item(i);
			
			String username = e.getAttribute("username");
			String password = e.getAttribute("password");
			String roles = e.getAttribute("roles");
			String[] roleList = roles.split(",");
			
			String role = "guest";
			for (String roleString : roleList)
				if (roleString.equals("sbts"))
					role = "admin";
			
			usersJSON.getUsers().add(new UserJSON(username, role, password));
		}
		
		return usersJSON;
	}
	
	public void updateTomcatUsers(UserJSON[] usersJSON) throws Exception {
		Map<String, UserJSON> userMap = new HashMap<String, UserJSON>();
		String catalinaHome;

		UsersJSON originalUsersJSON = parse();
		for (UserJSON userJSON : originalUsersJSON.getUsers())
			userMap.put(userJSON.getName(), userJSON);
		
		boolean foundAdmin = false;
		for (UserJSON userJSON: usersJSON) {
			String password = userJSON.getPassword();
			if (password == null || password.equals("")) {
				UserJSON oldUser = userMap.get(userJSON.getName());
				if (oldUser == null || oldUser.getName().equals("")) {
					if (logger.isDebugEnabled()) logger.debug("No password was provided for user (" + userJSON.getName() +
							") and the user did not pre-exist");
					throw new Exception("No password was provided for user (" + userJSON.getName() +
							") and the user did not pre-exist");
				}
				userJSON.setPassword(oldUser.getPassword());
			}

			if ("admin".equals(userJSON.getRole()))
				userJSON.setRole("sbts");
			else
				userJSON.setRole("guest");

			if (userJSON.getName().equals("admin") && userJSON.getRole().equals("sbts")) {
				foundAdmin = true;
			}
		}
		if (!foundAdmin) {
			if (logger.isDebugEnabled()) logger.debug("The admin user must always exist");
			throw new Exception("The admin user must always exist");
		}
		
		catalinaHome = System.getenv("CATALINA_HOME");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			InputSource source = new InputSource(catalinaHome + "/conf/tomcat-users.xml");
			Document doc = docBuilder.parse(source);
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			NodeList users = (NodeList) xpath.evaluate("/tomcat-users/user",
					doc, XPathConstants.NODESET);
			
			for (int i = 0; i < users.getLength(); i++) {
				Node node = users.item(i);
				node.getParentNode().removeChild(node);
			}
			
			Node parentNode = ((NodeList) xpath.evaluate("/tomcat-users",
					doc, XPathConstants.NODESET)).item(0);
			
			for (UserJSON userJSON : usersJSON) {
				Element e = doc.createElement("user");
				e.setAttribute("username", userJSON.getName());
				e.setAttribute("password", userJSON.getPassword());
				e.setAttribute("roles", userJSON.getRole());
				parentNode.appendChild(e);
			}
		    
		    Transformer xformer = TransformerFactory.newInstance().newTransformer();
		    xformer.transform(new DOMSource(doc),
		    		new StreamResult(new File(catalinaHome + "/conf/newtomcat-users.xml")));
		    opLogger.info("Updated users");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		} catch (TransformerException e) {
			e.printStackTrace();
			opLogger.info("Problem updating the users file: " + e.getMessage());
			throw new Exception("Problem updating the users file: " + e.getMessage());
		}
		
	}
}
