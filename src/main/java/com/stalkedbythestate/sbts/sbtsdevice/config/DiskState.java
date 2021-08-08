package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public enum DiskState {
	UN_INITIALISED,
	NO_DISK,
	NO_PARTITION,
	PARTITION_NOT_RIGHT_TYPE,
	FORMATTING,
	PARTITION_NOT_MOUNTED,
	PARTITION_MOUNTED_NO_STRUCTURE,
	ALL_GOOD;
}
