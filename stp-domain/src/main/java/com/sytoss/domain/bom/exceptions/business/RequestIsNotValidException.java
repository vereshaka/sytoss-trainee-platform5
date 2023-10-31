package com.sytoss.domain.bom.exceptions.business;

public class RequestIsNotValidException extends BusinessException {

    public RequestIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestIsNotValidException(String message) {
        super(message);
    }
}
