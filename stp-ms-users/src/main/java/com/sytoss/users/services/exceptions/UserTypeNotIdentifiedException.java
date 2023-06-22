package com.sytoss.users.services.exceptions;

import com.sytoss.domain.bom.exceptions.business.BusinessException;

public class UserTypeNotIdentifiedException extends BusinessException {

    public UserTypeNotIdentifiedException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
