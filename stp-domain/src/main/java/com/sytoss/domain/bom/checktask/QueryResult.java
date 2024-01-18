package com.sytoss.domain.bom.checktask;

import com.sytoss.domain.bom.checktask.exceptions.*;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

@Getter
public class QueryResult {

    private final List<Map<Integer, Object>> resultMapList = new ArrayList<>();

    @Setter
    private List<String> header = new ArrayList<>();

    @Setter
    private int affectedRowsCount = 0;

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

    public Set<Exception> compareWithEtalon(QueryResult etalon, CheckRequestParameters checkRequestParameters) {
        Set<Exception> result = new HashSet<>();
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
            WrongSortingException wrongSortingException = new WrongSortingException();
            for (int index : columnIndexMapping.keySet()) {
                int answerIndex = columnIndexMapping.get(index);
                if (answerIndex == -1) {
                    absentColumns.add(etalon.getHeader().get(index));
                } else {
                    List<Object> etalonColumnValues = etalon.getColumnValue(index);
                    List<Object> answerColumnValues = getColumnValue(answerIndex);
                    if (!CollectionUtils.isEqualCollection(etalonColumnValues, answerColumnValues)) {
                        wrongOrderingColumns.add(etalon.getHeader().get(index));
                    }
                    if (checkRequestParameters.isSortingRelevant() && !etalonColumnValues.equals(answerColumnValues)) {
                        result.add(wrongSortingException);
                    }
                }
            }
            if (absentColumns.size() > 0 && absentColumns.size() == etalon.getHeader().size()) {
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
