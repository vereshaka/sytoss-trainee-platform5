package com.sytoss.domain.bom.exceptions.business;

public class AnswerIsAnsweredException extends BusinessException {

    public AnswerIsAnsweredException() {
        super("Answer is already answered!");
    }
}
