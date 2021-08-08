package com.stalkedbythestate.sbts.eh;

// Copyright (c) 2021 Kim Hendrikse


public interface SyntheticEventManager {

	public void subscribe(EventListener listener, long timeRange, String resultDescription,
                          String... eventDescriptions);

	public void fireSynthetics(String eventDescription);
}
