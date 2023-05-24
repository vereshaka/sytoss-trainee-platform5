package com.sytoss.domain.bom.exceptions.businessException;

import com.sytoss.domain.bom.exceptions.ApplicationException;

public abstract class BusinessException extends ApplicationException {

    public BusinessException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
