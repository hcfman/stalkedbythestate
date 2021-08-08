package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EmailConfig {
	private EmailProvider emailProvider;


	public void addEmailConfig(Document doc, Element parent) {
		if (emailProvider == null || emailProvider.getName() == null
				|| emailProvider.getName().trim().equals(""))
			return;

		Element emailElement = doc.createElement("email");
		parent.appendChild(emailElement);
		emailElement.setAttribute("name", emailProvider.getName().trim());
		
		String description = emailProvider.getDescription();
		if (description != null)
			emailElement.setAttribute("description", description.trim());

		emailElement.appendChild(doc.createTextNode("\n\t"));
		Element descriptionElement = doc.createElement("description");
		descriptionElement.appendChild(doc.createTextNode(description));
		emailElement.appendChild(descriptionElement);

		emailElement.appendChild(doc.createTextNode("\n\t"));
		Element mailhostElement = doc.createElement("mailhost");
		mailhostElement.appendChild(doc.createTextNode(emailProvider.getMailhost()));
		emailElement.appendChild(mailhostElement);

		emailElement.appendChild(doc.createTextNode("\n\t"));
		Element fromElement = doc.createElement("from");
		fromElement.appendChild(doc.createTextNode(emailProvider.getFrom()));
		emailElement.appendChild(fromElement);

		String username = emailProvider.getUsername();
		if (username != null && !username.trim().equals("")) {
			emailElement.appendChild(doc.createTextNode("\n\t"));
			Element usernameElement = doc.createElement("username");
			usernameElement.appendChild(doc.createTextNode(username.trim()));
			emailElement.appendChild(usernameElement);
		}

		String password = emailProvider.getPassword();
		if (password != null && !password.trim().equals("")) {
			emailElement.appendChild(doc.createTextNode("\n\t"));
			Element passwordElement = doc.createElement("password");
			passwordElement.appendChild(doc.createTextNode(password.trim()));
			emailElement.appendChild(passwordElement);
		}

		String encTypeString = emailProvider.getEncryptionType().toString();
		if (encTypeString != null && !encTypeString.trim().equals("")) {
			emailElement.appendChild(doc.createTextNode("\n\t"));
			Element encTypeElement = doc.createElement("encType");
			encTypeElement.appendChild(doc.createTextNode(encTypeString.trim()));
			emailElement.appendChild(encTypeElement);
		}


		
		
		emailElement.appendChild(doc.createTextNode("\n\t"));
		Integer port = emailProvider.getPort();
		if (port == null || port == 0)
			port = 25;
		
		Element portElement = doc.createElement("port");
		portElement.appendChild(doc.createTextNode(Integer.toString(port)));
		emailElement.appendChild(portElement);

		emailElement.appendChild(doc.createTextNode("\n"));
		parent.appendChild(doc.createTextNode("\n\n"));
	}
	
	public EmailProvider getEmailProvider() {
		return emailProvider;
	}

	public void setEmailProvider(EmailProvider emailProvider) {
		this.emailProvider = emailProvider;
	}

}
