package com.sytoss.lessons.controllers.filter;

import com.sytoss.lessons.controllers.api.FilterItem;

import java.util.List;

public class DisciplineFilterSet implements FilterSet {
    @Override
    public List<FilterItem> getFilters() {

        FilterItem nameFilterItem = new FilterItem();
        nameFilterItem.setFieldName("name");

        return List.of(nameFilterItem);
    }
}
