package com.stalkedbythestate.sbts.timeRanges;

import java.util.Calendar;
import java.util.Date;

public class TimeRange {
	private int startHour, startMin, startSec, endHour, endMin, endSec;

	public TimeRange() {
	}
	
	public TimeRange(int startHour, int startMin, int startSec, int endHour,
			int endMin, int endSec) {
		this.startHour = startHour;
		this.startMin = startMin;
		this.startSec = startSec;
		this.endHour = endHour;
		this.endMin = endMin;
		this.endSec = endSec;
	}
	
	public TimeRange(String startTime, String endTime) throws Exception {
		if (startTime == null && endTime == null)
			throw new Exception("Invalid time specification");
		
		String[] startParts = startTime.split(":");
		int startHour = Integer.parseInt(startParts[0]);
		int startMin = Integer.parseInt(startParts[1]);
		int startSec = Integer.parseInt(startParts[2]);
		
		String[] endParts = endTime.split(":");
		int endHour = Integer.parseInt(endParts[0]);
		int endMin = Integer.parseInt(endParts[1]);
		int endSec = Integer.parseInt(endParts[2]);
		
		this.startHour = startHour;
		this.startMin = startMin;
		this.startSec = startSec;
		this.endHour = endHour;
		this.endMin = endMin;
		this.endSec = endSec;
	}
	
	public boolean within(long timeValue) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timeValue));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timeValue));
//		System.out.println("timeRange within: ");
//		System.out.println("timeRange within: candidate hour: " + calendar.get(Calendar.HOUR_OF_DAY));
//		System.out.println("timeRange within: candidate min: " + calendar.get(Calendar.MINUTE));
//		System.out.println("timeRange within: candidate sec: " + calendar.get(Calendar.SECOND));
		cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, 0);
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(new Date(timeValue));
		startCal.set(Calendar.HOUR_OF_DAY, startHour);
		startCal.set(Calendar.MINUTE, startMin);
		startCal.set(Calendar.SECOND, startSec);
		startCal.set(Calendar.MILLISECOND, 0);

//		System.out.println("timeRange within: start hour: " + startCal.get(Calendar.HOUR_OF_DAY));
//		System.out.println("timeRange within: start min: " + startCal.get(Calendar.MINUTE));
//		System.out.println("timeRange within: start sec: " + startCal.get(Calendar.SECOND));

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(new Date(timeValue));
		endCal.set(Calendar.HOUR_OF_DAY, endHour);
		endCal.set(Calendar.MINUTE, endMin);
		endCal.set(Calendar.SECOND, endSec);
		endCal.set(Calendar.MILLISECOND, 0);
		
//		System.out.println("timeRange within: end hour: " + endCal.get(Calendar.HOUR_OF_DAY));
//		System.out.println("timeRange within: end min: " + endCal.get(Calendar.MINUTE));
//		System.out.println("timeRange within: end sec: " + endCal.get(Calendar.SECOND));

		if ( cal.before(startCal)) {
//			System.out.println("Candidate is less than the start, candidate is: " + cal);
			return false;
		}
		
		if (cal.after(endCal)) {
//			System.out.println("Candidate is greater than the end, candidate is: " + cal);
			return false;
		}
		
		return true;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	public int getStartSec() {
		return startSec;
	}

	public void setStartSec(int startSec) {
		this.startSec = startSec;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMin() {
		return endMin;
	}

	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}

	public int getEndSec() {
		return endSec;
	}

	public void setEndSec(int endSec) {
		this.endSec = endSec;
	}

	@Override
	public String toString() {
		return "TimeRange [endHour=" + endHour + ", endMin=" + endMin
				+ ", endSec=" + endSec + ", startHour=" + startHour
				+ ", startMin=" + startMin + ", startSec=" + startSec + "]";
	}
	
}
