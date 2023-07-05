package com.sytoss.users.services.exceptions;

import com.sytoss.domain.bom.exceptions.business.BusinessException;

public class UserPhotoNotFoundException extends BusinessException {

    public UserPhotoNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
