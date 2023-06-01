package com.sytoss.domain.bom.exceptions.business.notfound;

public class TopicNotFoundException extends NotFoundException {

    public TopicNotFoundException(Long id) {
        super("Topic", id);
    }
}
