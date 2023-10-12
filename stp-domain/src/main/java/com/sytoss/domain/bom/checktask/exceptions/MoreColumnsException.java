package com.sytoss.domain.bom.checktask.exceptions;

public class MoreColumnsException extends RuntimeException {
    public MoreColumnsException() {
        super("There are more columns in the answer than in the etalon");
    }
}
