package com.sytoss.domain.bom.convertors;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DataRow {

    private String raw;
    private Map<String, String> values = new HashMap<>();
}
