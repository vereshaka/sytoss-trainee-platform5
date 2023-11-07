package com.sytoss.lessons.controllers.filter;

import com.sytoss.lessons.controllers.api.FilterItem;
import com.sytoss.lessons.dto.DisciplineDTO;

import java.util.HashMap;
import java.util.List;

public class FilterFactory {

    private static HashMap<Class, FilterSet> filters;

    static {
        filters = new HashMap<>();
        filters.put(DisciplineDTO.class, new DisciplineFilterSet());
    }

    public static List<FilterItem> getFilterSet(Class type){
        return filters.get(type).getFilters();
    }
}
