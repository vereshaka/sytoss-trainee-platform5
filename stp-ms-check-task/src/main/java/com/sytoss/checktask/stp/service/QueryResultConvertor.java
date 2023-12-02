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
            String columnName = metaData.getColumnName(i);

            if (header.contains(columnName) || columnName.isEmpty()) {
                if (columnName.isEmpty()) {
                    columnName = "COLUMN";
                }
                for (int j = 1; j <= columns; j++) {
                    if (!header.contains(columnName + "_" + j)) {
                        columnName = columnName + "_" + j;
                        break;
                    }
                }
            }
            header.add(columnName);

        }

        destination.setHeader(header);
        //TODO: yevgenyv: hsqldb: source.beforeFirst();

        while (source.next()) {
            HashMap<String, Object> row = new HashMap<>();
            for(int i = 1; i<=columns; i++){
                String columnName = header.get(i-1);
                row.put(columnName,source.getObject(i));
            }
            destination.addValues(row);
        }

        return destination;
    }
}

