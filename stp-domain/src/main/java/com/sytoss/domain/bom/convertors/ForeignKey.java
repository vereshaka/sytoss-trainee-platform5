package com.sytoss.domain.bom.convertors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForeignKey {

    private String targetTable;

    private String targetColumn;
}
