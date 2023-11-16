package com.sytoss.domain.bom.exceptions.business.notfound;

public class RatingNotFoundException extends NotFoundException {

    public RatingNotFoundException(Long id) {
        super("Rating", id);
    }
}
