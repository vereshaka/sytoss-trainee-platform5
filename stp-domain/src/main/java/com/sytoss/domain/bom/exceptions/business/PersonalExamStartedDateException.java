package com.sytoss.domain.bom.exceptions.business;

public class PersonalExamStartedDateException extends BusinessException{
    public PersonalExamStartedDateException() {
        super("Personal exam cannot start before started date");
    }
}
