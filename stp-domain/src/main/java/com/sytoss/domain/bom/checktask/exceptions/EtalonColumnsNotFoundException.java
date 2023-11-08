package com.sytoss.domain.bom.checktask.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class EtalonColumnsNotFoundException extends RuntimeException {

    private final List<String> absentColumns;

    public EtalonColumnsNotFoundException(List<String> absentColumns) {
        super(String.join(";",absentColumns)+" columns are absent in the answer");
        this.absentColumns = absentColumns;
    }
}
