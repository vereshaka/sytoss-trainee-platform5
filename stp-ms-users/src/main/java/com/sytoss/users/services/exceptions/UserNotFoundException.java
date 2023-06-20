package com.sytoss.users.services.exceptions;

import com.sytoss.domain.bom.exceptions.business.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
