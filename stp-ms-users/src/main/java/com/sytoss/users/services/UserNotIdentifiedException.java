package com.sytoss.users.services;

import com.sytoss.domain.bom.exceptions.business.BusinessException;

public class UserNotIdentifiedException extends BusinessException {

    public UserNotIdentifiedException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
