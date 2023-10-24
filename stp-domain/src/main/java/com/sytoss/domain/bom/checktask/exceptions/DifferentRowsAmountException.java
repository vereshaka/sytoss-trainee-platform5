package com.sytoss.domain.bom.checktask.exceptions;

public class DifferentRowsAmountException extends RuntimeException {
    public DifferentRowsAmountException() {
        super("Amount of data is different");
    }
}
