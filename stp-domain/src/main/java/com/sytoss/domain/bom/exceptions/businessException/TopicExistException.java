package com.sytoss.domain.bom.exceptions.businessException;

public class TopicExistException extends AlreadyExistException {

    public TopicExistException(String name) {
        super("Topic", name);
    }

}
