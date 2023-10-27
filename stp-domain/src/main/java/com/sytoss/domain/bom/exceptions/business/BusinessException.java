package com.sytoss.domain.bom.exceptions.business;

import com.sytoss.domain.bom.exceptions.ApplicationException;

public abstract class BusinessException extends ApplicationException {

    public BusinessException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public BusinessException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
