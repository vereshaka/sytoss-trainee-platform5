package com.sytoss.domain.bom.exceptions.businessException;

public class AnswerIsAnsweredException extends BusinessException {

    public AnswerIsAnsweredException() {
        super("Answer is already answered!");
    }
}
