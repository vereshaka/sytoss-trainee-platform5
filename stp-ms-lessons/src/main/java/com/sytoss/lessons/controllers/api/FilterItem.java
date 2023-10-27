package com.sytoss.lessons.controllers.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterItem {

    private String fieldName;
    private String value;
    private String type;
    private List<String> possibleValues;

}
