package com.ims.exception;

@SuppressWarnings("serial")
public class DuplicateSerialNumberException extends RuntimeException {

	public DuplicateSerialNumberException(String message) {
		super(message);
	}

	public DuplicateSerialNumberException(String message, Throwable cause) {
		super(message, cause);
	}
}
