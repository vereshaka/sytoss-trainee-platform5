package com.sytoss.domain.bom.exceptions.business;

public class TopicExistException extends AlreadyExistException {

    public TopicExistException(String name) {
        super("Topic", name);
    }

}
