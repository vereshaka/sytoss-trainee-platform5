package com.sytoss.domain.bom.checktask.exceptions;

public class WrongDataException extends RuntimeException {
    public WrongDataException() {
        super("Request data is wrong comparable to etalon data");
    }
}
