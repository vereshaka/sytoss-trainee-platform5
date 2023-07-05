package com.sytoss.domain.bom.exceptions;

import com.sytoss.domain.bom.exceptions.business.BusinessException;
import lombok.Getter;

@Getter
public class ApplicationError {

    private final String message;

    private final boolean isTechnical;

    public ApplicationError(Exception exception) {
        this.message = exception.getMessage();
        isTechnical = exception instanceof BusinessException;
    }
}
