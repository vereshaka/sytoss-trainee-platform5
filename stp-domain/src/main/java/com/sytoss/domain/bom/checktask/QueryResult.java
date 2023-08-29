package com.sytoss.domain.bom.checktask;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueryResult {
    private final List<Map<Integer, Object>> resultMapList = new ArrayList<>();
    @Setter
    private List<String> header = new ArrayList<>();

    public void addValues(Map<String, Object> row) {
        Map<Integer, Object> tableRow = new HashMap<>();
        for (String columnName : header) {
            int columnIndex = header.indexOf(columnName);
            tableRow.put(columnIndex, row.get(columnName));
        }
        resultMapList.add(tableRow);
    }

    public Object getValue(int rowNumber, String columnName) {
        int columnIndex = header.indexOf(columnName);
        return resultMapList.get(rowNumber).get(columnIndex);
    }

}
