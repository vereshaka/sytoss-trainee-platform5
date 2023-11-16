package com.sytoss.domain.bom.exceptions.business.notfound;

public class AnalyticsNotFoundException extends NotFoundException {

    public AnalyticsNotFoundException(Long id) {
        super("Analytics Element", id);
    }
}
