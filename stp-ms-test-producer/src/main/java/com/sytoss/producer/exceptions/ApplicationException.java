package com.sytoss.producer.exceptions;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
