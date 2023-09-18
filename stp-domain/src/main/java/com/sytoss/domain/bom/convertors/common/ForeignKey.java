package com.sytoss.domain.bom.convertors.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForeignKey {

    private String targetTable;

    private String targetColumn;
}
