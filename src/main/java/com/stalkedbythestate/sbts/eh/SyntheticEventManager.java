package com.stalkedbythestate.sbts.eh;


public interface SyntheticEventManager {

	public void subscribe(EventListener listener, long timeRange, String resultDescription,
                          String... eventDescriptions);

	public void fireSynthetics(String eventDescription);
}
