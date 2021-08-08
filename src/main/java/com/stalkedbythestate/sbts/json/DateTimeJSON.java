package com.stalkedbythestate.sbts.json;

// Copyright (c) 2021 Kim Hendrikse

public class DateTimeJSON {
	private String date;
	private int timeHour;
	private int timeMinute;
	private String timeZone;
	private String ntpServer;
	private boolean useNtp = true;

	public DateTimeJSON() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getTimeHour() {
		return timeHour;
	}

	public void setTimeHour(int timeHour) {
		this.timeHour = timeHour;
	}

	public int getTimeMinute() {
		return timeMinute;
	}

	public void setTimeMinute(int timeMinute) {
		this.timeMinute = timeMinute;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getNtpServer() {
		return ntpServer;
	}

	public void setNtpServer(String ntpServer) {
		this.ntpServer = ntpServer;
	}

	public boolean isUseNtp() {
		return useNtp;
	}

	public void setUseNtp(boolean useNtp) {
		this.useNtp = useNtp;
	}

	@Override
	public String toString() {
		return "DateTimeJSON [date=" + date + ", ntpServer=" + ntpServer
				+ ", timeHour=" + timeHour + ", timeMinute=" + timeMinute
				+ ", timeZone=" + timeZone + ", useNtp=" + useNtp + "]";
	}

}
