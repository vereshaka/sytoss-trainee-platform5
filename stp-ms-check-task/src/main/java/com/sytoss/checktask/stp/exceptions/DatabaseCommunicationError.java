package com.sytoss.checktask.stp.exceptions;

public class DatabaseCommunicationError extends RuntimeException {

    public DatabaseCommunicationError(String message) {
        super(message);
    }
}
