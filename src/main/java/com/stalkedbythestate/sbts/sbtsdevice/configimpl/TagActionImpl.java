package com.stalkedbythestate.sbts.sbtsdevice.configimpl;

import com.stalkedbythestate.sbts.sbtsdevice.config.AbstractAction;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.sbtsdevice.config.TagActionType;

public class TagActionImpl extends AbstractAction {
	private TagActionType tagActionType;
	private String tagName;
	private Long validFor;

	public TagActionImpl(String name, String eventName, String description, TagActionType tagActionType, String tagName) {
		setName(name);
		if (tagActionType == TagActionType.SET_ON)
			setActionType(ActionType.ACTION_ADD_TAG);
		else
			setActionType(ActionType.ACTION_DELETE_TAG);
			
		setEventName(eventName);
		setDescription(description);
		this.tagName = tagName;
		this.tagActionType = tagActionType;
	}

	public TagActionType getTagActionType() {
		return tagActionType;
	}

	public void setProfileActionType(TagActionType tagActionType) {
		this.tagActionType = tagActionType;
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Long getValidFor() {
		return validFor;
	}

	public void setValidFor(Long validFor) {
		this.validFor = validFor;
	}

	@Override
	public String toString() {
		return "TagActionImpl [tagActionType=" + tagActionType + ", tagName="
				+ tagName + ", validFor=" + validFor + ", Description="
				+ Description + ", eventName=" + eventName + ", name=" + name
				+ "]";
	}

}
