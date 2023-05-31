package com.sytoss.domain.bom.exceptions.business.notfound;

import com.sytoss.domain.bom.exceptions.business.BusinessException;

public class NotFoundException extends BusinessException {

    public NotFoundException(String objectName, String name) {

        super(objectName + " with name \"" + name + "\" not found");
    }

    public NotFoundException(String objectName, Long id) {

        super(objectName + " with id \"" + id + "\" not found");
    }
}
