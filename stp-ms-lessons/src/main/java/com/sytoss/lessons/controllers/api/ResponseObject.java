package com.sytoss.lessons.controllers.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseObject<T> {

    private List<T> data;
    private List<FilterField> filter;
    private PagingInfo paging;

}
