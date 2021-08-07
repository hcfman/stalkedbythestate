package com.stalkedbythestate.sbts.sbtsdevice.config;

import com.stalkedbythestate.sbts.timeRanges.TimeSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

public class ProfileConfig {
	private volatile SortedSet<Profile> profileList = new TreeSet<Profile>();
	private volatile Map<String,Profile> profileMap = new HashMap<String, Profile>();

	public void add(Profile profile) {
		profileList.remove(profile);
		profileList.add(profile);
		profileMap.remove(profile);
		profileMap.put(profile.getTagName(), profile);
	}
	
	public Profile get(String name) {
		return profileMap.get(name);
	}
	
	public SortedSet<Profile> getProfileList() {
		return profileList;
	}

	public void setProfileList(SortedSet<Profile> profileList) {
		this.profileList = profileList;
	}

	public void addProfileConfig(Document doc, Element parent) {
		Element profiles = doc.createElement("profiles");
		for (Profile profile : profileList) {
			profiles.appendChild(doc.createTextNode("\n\t"));
			Element profileElement = doc.createElement("profile");
			profiles.appendChild(profileElement);
			profileElement.setAttribute("name", profile.getName());
			profileElement.setAttribute("tagName", profile.getTagName());
			profileElement.setAttribute("description", profile.getDescription());
			
			Collection<TimeSpec> validTimesList = profile.getValidTimes();
			if (validTimesList != null && validTimesList.size() > 0) {
				Element validTimesElement = TimeSpecListToXML.toXML(doc, validTimesList, 2);
				profileElement.appendChild(doc.createTextNode("\n" + StringHelper.repeatChar('\t', 2)));
				profileElement.appendChild(validTimesElement);
				profileElement.appendChild(doc.createTextNode("\n\t"));
			}
			
			Boolean isOn = profile.getIsOn();
			if (isOn != null && isOn) {
				profileElement.appendChild(doc.createTextNode("\n\t\t"));
				Element isOnElement = doc.createElement("isOn");
				profileElement.appendChild(isOnElement);
				isOnElement.setAttribute("value", "true");
				profileElement.appendChild(doc.createTextNode("\n\t"));
			}
		}
		profiles.appendChild(doc.createTextNode("\n"));
		parent.appendChild(profiles);
	}
}
