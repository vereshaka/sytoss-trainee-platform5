package com.sytoss.lessons.controllers.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagingInfo {

    private long totalRecCount;
    private int currentPage;
    private int pageSize;

}
