package com.stalkedbythestate.sbts.freak.api;

import com.stalkedbythestate.sbts.eventlib.Event;
import com.stalkedbythestate.sbts.eventlib.EventListener;
import com.stalkedbythestate.sbts.sbtsdevice.config.SbtsDeviceConfig;
import com.stalkedbythestate.sbts.rfxcomhandler.RfxcomHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public interface FreakApi {

	public void start();

	public boolean isReady();

	public void sendEvent(Event event);

	public void sendEmailEvent(Event event);

	public void sendRfxcomEvent(Event event);

	public void sendDvrEvent(Event event);

	public void sendHttpEvent(Event event);

	public void sendPhidgetEvent(Event event);

	public List<String> getTagList();

	public void clearTags(List<String> tagList);

	public AtomicBoolean getUpdating();

	public SbtsDeviceConfig getSbtsConfig();

	public boolean saveConfig();

	public String getSbtsBase();

	public boolean mountReadonly();

	public boolean mountReadWrite();

	public void subscribeEvents(EventListener listener, String subscription);

	public Map<EventListener, Pattern> getListenerMap();

	public void unsubscribeEvents(EventListener listener);

	public RfxcomHandler getRfxcomHandler();
};
