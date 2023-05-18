package com.sytoss.producer.exceptions.businessException;

import com.sytoss.producer.exceptions.ApplicationException;

public abstract class BusinessException extends ApplicationException {

    public BusinessException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
