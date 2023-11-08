package com.sytoss.domain.bom.convertors.common;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class DataRow {

    private String raw;
    private Map<String, String> values = new LinkedHashMap<>();
}
