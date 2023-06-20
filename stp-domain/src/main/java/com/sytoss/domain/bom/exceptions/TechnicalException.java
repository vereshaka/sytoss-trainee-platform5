package com.sytoss.domain.bom.exceptions;

public abstract class TechnicalException extends ApplicationException {

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
