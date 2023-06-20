package com.sytoss.users.services.exceptions;

import com.sytoss.domain.bom.exceptions.TechnicalException;

public class LoadImageException extends TechnicalException {

    public LoadImageException(String exceptionMessage, Throwable t) {
        super(exceptionMessage, t);
    }
}