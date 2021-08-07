package com.stalkedbythestate.sbts.rfxcomlib;

public enum RfxcomOperator {
	EQ,
	GE,
	GT,
	LE,
	LT,
	RANGE;
	
	public static RfxcomOperator set(String value) throws RuntimeException {
		if (value.equals("EQ"))
			return EQ;
		if (value.equals("GE"))
			return GE;
		if (value.equals("GT"))
			return GT;
		if (value.equals("LE"))
			return LE;
		if (value.equals("LT"))
			return LT;
		if (value.equals("RANGE"))
			return RANGE;
		
		throw new RuntimeException(
				"Invalid value specified for RfxcomOperator Enum: " + value);
	}

}
