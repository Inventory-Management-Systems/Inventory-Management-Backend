package com.ims.exception;

@SuppressWarnings("serial")
public class InvalidEmailOrPasswordException extends RuntimeException {

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }

    public InvalidEmailOrPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
