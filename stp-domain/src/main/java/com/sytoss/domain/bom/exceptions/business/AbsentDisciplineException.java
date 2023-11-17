package com.sytoss.domain.bom.exceptions.business;

public class AbsentDisciplineException extends BusinessException {

    public AbsentDisciplineException() {

        super("discipline cannot have null id");
    }
}
