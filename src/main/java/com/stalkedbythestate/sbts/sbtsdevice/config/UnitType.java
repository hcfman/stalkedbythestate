package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

import java.util.ArrayList;
import java.util.List;

public enum UnitType {
	sec,
	ms;
	
	public static UnitType set(String value) throws RuntimeException {
		if (value.equals("sec"))
			return sec;
		if (value.equals("ms"))
			return ms;
		throw new RuntimeException(
				"Invalid value specified for UnitType Enum: " + value);
	}
	
	public static List<String> getUnitTypeList() {
		List<String> unitTypeList = new ArrayList<String>();
		for (UnitType unitType : UnitType.values()) {
			unitTypeList.add(unitType.toString());
		}
		
		return unitTypeList;
	}

}
