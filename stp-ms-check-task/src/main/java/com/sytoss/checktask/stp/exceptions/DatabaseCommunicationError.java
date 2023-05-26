package com.sytoss.checktask.stp.exceptions;

public class DatabaseCommunicationError extends RuntimeException {
    public DatabaseCommunicationError(String message) {
        super(message);
    }
    public DatabaseCommunicationError(String message, Exception exception) {
        super(message);
        exception.printStackTrace();
    }
}
