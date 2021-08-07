package com.stalkedbythestate.sbts.email;

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.sbtsdevice.config.Action;
import com.stalkedbythestate.sbts.sbtsdevice.config.EmailConfig;
import com.stalkedbythestate.sbts.sbtsdevice.config.EmailProvider;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.EmailActionImpl;
import com.stalkedbythestate.sbts.sbtsdevice.configimpl.VideoType;
import com.stalkedbythestate.sbts.freak.api.FreakApi;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleEmail implements Runnable {
	private static final Logger logger = Logger.getLogger(HandleEmail.class);
	private static final Logger opLogger = Logger.getLogger("operations");
	private Event event;
	private Event originalEvent;
	private Action action;
	private String emailTemplate;
	private FreakApi freak;

	public HandleEmail(FreakApi freak, Event event, Event originalEvent,
                       Action action, String emailTemplate) {
		this.freak = freak;
		this.event = event;
		this.originalEvent = originalEvent;
		this.action = action;

		this.emailTemplate = emailTemplate;
	}

	private class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator(String username, String password) {
			authentication = new PasswordAuthentication(username, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}

	private String[] getTemplateParts(String template) {
		if (template == null)
			return null;

		String[] parts = new String[3];
		Pattern pattern = Pattern.compile("^(?s)(.*)<loop>(.*)</loop>(.*)$");
		Matcher matcher = pattern.matcher(template);
		if (!matcher.find())
			return null;

		parts[0] = matcher.group(1);
		parts[1] = matcher.group(2);
		parts[2] = matcher.group(3);

		return parts;
	}

	private static String encodeHTML(String s) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 127 || c == '"' || c == '<' || c == '>') {
				out.append("&#" + (int) c + ";");
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	@Override
	public void run() {
		SbtsDeviceConfig sbtsConfig = freak.getSbtsConfig();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
		String dateString;
		StringBuffer sb = new StringBuffer();
		String body = null;
		String subject = null;

		EmailActionImpl emailActionImpl = (EmailActionImpl) action;
		String viewType = emailActionImpl.getVideoType() == VideoType.PJPEG ? "pview"
				: "view";

		EmailConfig emailConfig = sbtsConfig.getEmailConfig();
		if (emailConfig == null)
			return;

		if (logger.isDebugEnabled())
			logger.debug("Got emailConfig");

		EmailProvider emailProvider = emailConfig.getEmailProvider();
		if (emailProvider == null) {
			opLogger.error("E-mail has not been setup yet");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Got email provider");
			opLogger.error("E-mail has not been setup yet");
		}

		String from = emailProvider.getFrom();
		if (from == null || from.trim().equals("")) {
			opLogger.error("You haven't set the from address for your E-mail yet");
			return;
		}

		if (!from.matches("^.*@.*$")) {
			opLogger.error("The from value of the E-mail provider must be an E-mail address");
			return;
		}

		if (from.contains(" \t")) {
			opLogger.error("The E-mail address in the from field may not contain spaces: contains("
					+ from + ")");
			return;
		}

		if (logger.isDebugEnabled())
			logger.debug("got from data");

		String to = emailActionImpl.getTo();
		if (to == null || to.trim().equals("")) {
			opLogger.error("You haven't specified a E-mail address to mail to");
			return;
		}

		if (logger.isDebugEnabled())
			logger.debug("Got to data, green for mailing");

		boolean plain = false;

		String parts[] = null;
		if (!plain)
			parts = getTemplateParts(emailTemplate);

		if (parts == null)
			plain = true;

		// Replace switch with this
		sb.setLength(0);
		if (!plain) {
			String part0 = parts[0];
			String responseGroup = emailActionImpl.getResponseGroup();
			if (responseGroup == null || responseGroup.equals(""))
				part0 = part0
						.replaceAll("<has-response>.*?</has-response>", "");
			else {
				part0 = part0.replaceAll("</?has-response>", "");
				try {
					part0 = part0
							.replaceAll(
									"\\$\\{responseUrl\\}",
									sbtsConfig.getSettingsConfig()
											.getWebPrefix()
											+ "buttons?group="
											+ URLEncoder.encode(responseGroup,
													"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("Cannot encode response group link", e);
				}
			}

			sb.append(part0.replaceAll("\\$\\{title\\}",
					encodeHTML(action.getDescription())));
		}
		for (Integer i : emailActionImpl.getCameraSet()) {
			if (plain) {
				sb.append("\n\n");
				sb.append(sbtsConfig.getSettingsConfig().getWebPrefix());
				sb.append(viewType + "?t=");
				sb.append(event.getEventTime());
				sb.append("&cam=");
				sb.append(i.toString());
			} else {
				sb.append(parts[1]
						.replaceAll(
								"\\$\\{cameraName\\}",
								encodeHTML(sbtsConfig.getCameraConfig().get(i)
										.getName())).replaceAll(
								"\\$\\{url\\}",
								sbtsConfig.getSettingsConfig().getWebPrefix()
										+ viewType + "?t="
										+ event.getEventTime() + "&cam="
										+ i.toString()));
			}
		}

		if (!plain)
			sb.append(parts[2]);

		body = sb.toString();
		dateString = sdf.format(new Date(originalEvent.getEventTime()));
		subject = dateString + action.getDescription();
		body = sb.toString();
		// Replace switch with this

		if (logger.isDebugEnabled())
			logger.debug("ACTION_EMAIL");

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", emailProvider.getMailhost());
		properties.setProperty("mail.smtp.port",
				Integer.toString(emailProvider.getPort()));

		// Get the default Session object.
		Authenticator authenticator = null;
		String username = emailProvider.getUsername();
		String password = emailProvider.getPassword();
		Session session = null;
		if (username != null && !username.equals("") && password != null
				&& !password.equals("")) {
			authenticator = new Authenticator(username, password);
			// session = Session.getDefaultInstance(properties, authenticator);
			session = Session.getInstance(properties, authenticator);

			properties.setProperty("mail.smtp.submitter", authenticator
					.getPasswordAuthentication().getUserName());
			properties.setProperty("mail.smtp.auth", "true");

			/* Set defaults for encryption to plain */
			properties.remove("mail.smtp.socketFactory.port");
			properties.remove("mail.smtp.socketFactory.class");
			properties.remove("mail.smtp.socketFactory.fallback");
			properties.remove("mail.smtp.starttls.enable");

			/* Is this encrypted? */
			switch (emailProvider.getEncryptionType()) {
			case ENC_PLAIN:
				break;
			case ENC_SSL:
				properties.put("mail.smtp.socketFactory.port",
						Integer.toString(emailProvider.getPort()));
				properties.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				properties.put("mail.smtp.socketFactory.fallback", "false");
				break;
			case ENC_TLS:
				properties.put("mail.smtp.starttls.enable", "true");
				break;
			}

			if (logger.isDebugEnabled())
				logger.debug("Using authenticated E-mail");
		} else {
			session = Session.getInstance(properties);
			properties.remove("mail.smtp.submitter");
			properties.remove("mail.smtp.auth");
			if (logger.isDebugEnabled())
				logger.debug("Using un-authenticated E-mail");
		}
		// session = Session.getDefaultInstance(properties);

		if (logger.isDebugEnabled())
			logger.debug("Sending from (" + from + ") to (" + to + ") subject("
					+ subject + ") body (" + body + ")");
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set the RFC 822 "From" header field using the
			// value of the InternetAddress.getLocalAddress method.
			message.setFrom(new InternetAddress(from));

			// Add the given addresses to the specified recipient type.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set the "Subject" header field.
			message.setSubject(subject);

			if (plain) {
				// Sets the given String as this part's content,
				// with a MIME type of "text/plain".
				message.setText(body);
			} else {
				Multipart outerPart = new MimeMultipart("mixed");
				MimeMultipart innerPart = new MimeMultipart("alternative");
				// Attach main part
				MimeBodyPart htmlBody = new MimeBodyPart();
				htmlBody.setContent(body, "text/html; charset=UTF-8");

				innerPart.addBodyPart(htmlBody);
				BodyPart alternativePart = new MimeBodyPart();
				alternativePart.setContent(innerPart);
				outerPart.addBodyPart(alternativePart);

				message.setContent(outerPart);
			}

			// Send message
			Transport.send(message);
		} catch (AddressException e) {
			opLogger.error("Address error whilst trying to send E-mail: "
					+ e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			opLogger.error("Error whilst trying to send E-mail: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}
}
