package com.sytoss.domain.bom.checktask.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class WrongOrderingColumnException extends RuntimeException {

    private final List<String> wrongOrderingColumns;

    public WrongOrderingColumnException(List<String> wrongOrderingColumns) {
        super("\n"+String.join(";",wrongOrderingColumns)+"\n columns are in the wrong order");
        this.wrongOrderingColumns = wrongOrderingColumns;
    }
}
