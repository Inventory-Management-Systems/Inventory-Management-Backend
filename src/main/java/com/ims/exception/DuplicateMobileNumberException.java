package com.ims.exception;

@SuppressWarnings("serial")
public class DuplicateMobileNumberException extends RuntimeException {

    public DuplicateMobileNumberException(String message) {
        super(message);
    }

    public DuplicateMobileNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
