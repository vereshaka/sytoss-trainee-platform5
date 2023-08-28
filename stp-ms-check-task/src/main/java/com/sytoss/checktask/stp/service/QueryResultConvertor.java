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

    public QueryResult convertFromResultSet(ResultSet source,QueryResult destination) throws SQLException {
        ResultSetMetaData metaData = source.getMetaData();
        int columns = source.getMetaData().getColumnCount();
        List<String> header = new ArrayList<>();
        for (int i = 1; i <= columns; i++) {
            header.add(metaData.getColumnName(i));
        }

        destination.setHeader(header);
        source.beforeFirst();

        while (source.next()) {
            HashMap<String, Object> row = new HashMap<>();
            for(int i = 1; i<=columns; i++){
                String columnName = metaData.getColumnName(i);
                row.put(columnName,source.getObject(columnName));
            }
            destination.addValues(row);
        }

        return destination;
    }
}

