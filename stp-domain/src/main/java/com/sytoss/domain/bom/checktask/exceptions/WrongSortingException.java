package com.sytoss.domain.bom.checktask.exceptions;

import lombok.Getter;

@Getter
public class WrongSortingException extends RuntimeException {
    public WrongSortingException() {
        super("Sorting is wrong.");
    }
}
