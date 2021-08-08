package com.stalkedbythestate.sbts.sbtsdevice.config;

// Copyright (c) 2021 Kim Hendrikse

public class ActionTypeException extends Exception {

	private static final long serialVersionUID = 4891405411888000565L;

	public ActionTypeException() {
	}

	public ActionTypeException(String message) {
		super(message);
	}

	public ActionTypeException(Throwable cause) {
		super(cause);
	}

	public ActionTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
