package com.sytoss.domain.bom.checktask;

import com.sytoss.domain.bom.checktask.exceptions.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueryResult {
    private final List<Map<Integer, Object>> resultMapList = new ArrayList<>();
    @Setter
    private List<String> header = new ArrayList<>();
    @Setter
    private int affectedRowsCount = 0;
    @Setter
    private boolean isRowsAffected = false;

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

    public List<Object> getColumnValue(int index) {
        List<Object> result = new ArrayList<>();
        resultMapList.forEach(item -> result.add(item.get(index)));
        return result;
    }

    public List<Exception> compareWithEtalon(QueryResult etalon) {
        List<Exception> result = new ArrayList<>();
        if (isRowsAffected) {
            if (etalon.getAffectedRowsCount() != this.getAffectedRowsCount()) {
                result.add(new DifferentRowsAmountException());  // case #2
            }
        } else {
            if (etalon.getResultMapList().size() != this.getResultMapList().size()) {
                result.add(new DifferentRowsAmountException());  // case #2
            } else {
                Map<Integer, Integer> columnIndexMapping = new HashMap<>();
                for (int i = 0; i < etalon.getHeader().size(); i++) {
                    List<Object> etalonColumn = etalon.getColumnValue(i);
                    columnIndexMapping.put(i, containsColumnByValue(etalonColumn));
                }

                List<String> absentColumns = new ArrayList<>();
                List<String> wrongOrderingColumns = new ArrayList<>();
                for (int index : columnIndexMapping.keySet()) {
                    int answerIndex = columnIndexMapping.get(index);
                    if (answerIndex == -1) {
                        absentColumns.add(etalon.getHeader().get(index));
                    } else {
                        if (!CollectionUtils.isEqualCollection(etalon.getColumnValue(index), getColumnValue(answerIndex))) {
                            wrongOrderingColumns.add(etalon.getHeader().get(index));
                        }
                    }
                }
                if (absentColumns.size() == etalon.getHeader().size()) {
                    result.add(new WrongDataException()); // case #3
                } else {
                    if (absentColumns.size() > 0) {
                        result.add(new EtalonColumnsNotFoundException(absentColumns)); // case #5
                    }
                    if (wrongOrderingColumns.size() > 0) {
                        result.add(new WrongOrderingColumnException(wrongOrderingColumns)); // case #4
                    }
                    if (etalon.getHeader().size() < getHeader().size()) {
                        result.add(new MoreColumnsException());  // case #6
                    }
                }
            }
        }

        return result;
    }

    private int containsColumnByValue(List<Object> etalonColumn) {
        for (int i = 0; i < getHeader().size(); i++) {
            List<Object> valueColumn = getColumnValue(i);
            if (CollectionUtils.containsAll(etalonColumn, valueColumn)) {
                return i;
            }
        }
        return -1;
    }
}
