package com.sytoss.domain.bom.convertors;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Column {

    private static final List<String> BOOLEAN_TYPES = List.of("BOOLEAN");
    private static final List<String> NUMBER_TYPES = List.of("LONG", "NUMBER", "INT");
    private static final List<String> DATE_TYPES = List.of("DATE", "DATETIME");

    private String name;
    private String datatype;
    private boolean primary;
    private ForeignKey foreignKey;
    private boolean nullable;

    public boolean isNumber() {
        return NUMBER_TYPES.contains(getTypeOnly());
    }

    public boolean isBoolean() {
        return BOOLEAN_TYPES.contains(getTypeOnly());
    }

    public boolean isDate() {
        return DATE_TYPES.contains(getTypeOnly());
    }

    private String getTypeOnly() {
        if (datatype.contains("\\(")) {
            return datatype.split("\\(")[0].trim();
        } else
            return datatype.trim().toUpperCase();
    }
}
