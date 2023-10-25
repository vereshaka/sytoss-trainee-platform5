package com.sytoss.lessons.controllers.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestObject {

    private List<FilterField> filter;

}
