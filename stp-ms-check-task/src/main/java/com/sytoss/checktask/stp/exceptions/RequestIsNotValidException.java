package com.sytoss.checktask.stp.exceptions;

public class RequestIsNotValidException extends RuntimeException {

    public RequestIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
