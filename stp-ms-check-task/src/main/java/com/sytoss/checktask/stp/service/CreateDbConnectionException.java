package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.exceptions.TechnicalException;

public class CreateDbConnectionException extends TechnicalException {
    public CreateDbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
