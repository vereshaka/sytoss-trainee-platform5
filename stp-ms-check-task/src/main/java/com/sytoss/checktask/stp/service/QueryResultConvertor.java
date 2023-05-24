package com.sytoss.checktask.stp.service;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class QueryResultConvertor {

    public List<HashMap<String, Object>> convertFromResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = resultSet.getMetaData().getColumnCount();
        List<HashMap<String, Object>> hashMapList = new ArrayList<>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(metaData.getColumnName(i), resultSet.getObject(i));
            }

            hashMapList.add(row);
        }

        return hashMapList;
    }
}

