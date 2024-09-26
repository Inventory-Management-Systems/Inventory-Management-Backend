package com.ims.exception;

@SuppressWarnings("serial")
public class DuplicateItemNameException extends RuntimeException {

	public DuplicateItemNameException(String message) {
		super(message);
	}

	public DuplicateItemNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
