package com.sytoss.domain.bom.exceptions.businessException;

public class AnswerInProgressException extends BusinessException{

    public AnswerInProgressException(){
        super("Answer is already in progress!");
    }
}
