package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.checktask.QueryResult;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class QueryResultConvertor {

    public QueryResult convertFromResultSet(QueryResult queryResult, ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = resultSet.getMetaData().getColumnCount();
        List<String> header = new ArrayList<>();
        for (int i = 1; i <= columns; i++) {
            header.add(metaData.getColumnName(i));
        }

        queryResult.setHeader(header);
        resultSet.beforeFirst();

        while (resultSet.next()) {
            HashMap<String, Object> row = new HashMap<>();
            for(int i = 1; i<=columns; i++){
                String columnName = metaData.getColumnName(i);
                row.put(columnName,resultSet.getObject(columnName));
            }
            queryResult.addValues(row);
        }

        return queryResult;
    }
}

