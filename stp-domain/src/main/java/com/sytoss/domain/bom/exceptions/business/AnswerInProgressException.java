package com.sytoss.domain.bom.exceptions.business;

public class AnswerInProgressException extends BusinessException{

    public AnswerInProgressException(){
        super("Answer is already in progress!");
    }
}
