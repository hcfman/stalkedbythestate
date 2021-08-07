package com.stalkedbythestate.sbts.json;


public class ButtonJSON {
	private String eventName;
	private String description;
	private boolean guest = false;

	public ButtonJSON() {
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	@Override
	public String toString() {
		return "ButtonJSON [description=" + description + ", eventName="
				+ eventName + ", guest=" + guest + "]";
	}

}
