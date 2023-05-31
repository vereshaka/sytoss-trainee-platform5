package com.sytoss.checktask.stp.exceptions;

public class DatabaseCommunicationException extends RuntimeException {

    public DatabaseCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
