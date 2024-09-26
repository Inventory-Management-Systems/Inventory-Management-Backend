package com.ims.exception;

@SuppressWarnings("serial")
public class DuplicateBillNumberException extends RuntimeException {

	public DuplicateBillNumberException(String message) {
		super(message);
	}

	public DuplicateBillNumberException(String message, Throwable cause) {
		super(message, cause);
	}
}
