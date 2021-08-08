package com.stalkedbythestate.sbts.eventlib;

// Copyright (c) 2021 Kim Hendrikse

public class SendHttpEvent extends AbstractEvent {
	private String fromAddress;
	private String toAddress;
	private String subject;
	private String body;
	
	public SendHttpEvent(String fromAddress, String toAddress, String subject, String body) {
		eventType = EventType.SEND_MAIL;
		
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.subject = subject;
		this.body = body;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int count = 0;
		
		if (fromAddress != null) {
			sb.append("fromAddress: " + fromAddress);
			count++;
		}
		
		if (toAddress != null) {
			if (count > 0)
				sb.append(", ");
			sb.append("toAddress: " + toAddress);
			count++;
		}
		
		if (subject != null) {
			if (count > 0)
				sb.append(", ");
			sb.append("subject: " + subject);
			count++;
		}
		
		if (body != null) {
			if (count > 0)
				sb.append(", ");
			sb.append("body: " + body);
			count++;
		}
		
		return sb.toString();
	}

}
