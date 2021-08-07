package com.stalkedbythestate.sbts.json;

import com.stalkedbythestate.sbts.sbtsdevice.config.FlagType;
import com.stalkedbythestate.sbts.sbtsdevice.config.ActionType;
import com.stalkedbythestate.sbts.timeRanges.TimeSpec;

import java.util.*;

public abstract class AbstractActionJSON {
	protected ActionType actionType;
	protected Set<FlagType> flags = new HashSet<FlagType>();
	protected String name;
	protected String eventName;
	protected String Description;
	protected Collection<TimeSpec> validTimes;
	protected SortedSet<String> positiveTagNames = new TreeSet<String>();
	protected SortedSet<String> negativeTagNames = new TreeSet<String>();
}
