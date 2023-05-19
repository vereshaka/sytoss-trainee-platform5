package com.sytoss.domain.bom.exceptions;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
