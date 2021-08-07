package com.stalkedbythestate.sbts.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TagButtonsJSON {
	private boolean result = false;
	private List<String> messages = new ArrayList<String>();
	private Collection<TagButton> tagButtons = new ArrayList<TagButton>();

	public boolean isResult() {
		return result;
	}

	public TagButtonsJSON(boolean result) {
		this.result = result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Collection<TagButton> getTagButtons() {
		return tagButtons;
	}

	public void setTagButtons(Collection<TagButton> tagButtons) {
		this.tagButtons = tagButtons;
	}

	@Override
	public String toString() {
		return "TagButtonsJSON [messages=" + messages + ", result=" + result
				+ ", tagButtons=" + tagButtons + "]";
	}

}
