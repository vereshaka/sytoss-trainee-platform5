package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.checktask.QueryResult;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class QueryResultConvertor {

    private final static List<Integer> INTEGER_TYPES = List.of(
            Types.BIGINT,
            Types.INTEGER,
            Types.SMALLINT,
            Types.TINYINT
    );

    private final static List<Integer> DECIMAL_TYPES = List.of(
            Types.DECIMAL,
            Types.DOUBLE,
            Types.FLOAT,
            Types.NUMERIC,
            Types.REAL

    );

    private final static List<Integer> DATE_TYPES = List.of(
            Types.DATE,
            Types.TIME,
            Types.TIMESTAMP,
            Types.TIME_WITH_TIMEZONE,
            Types.TIMESTAMP_WITH_TIMEZONE
    );

    public QueryResult convertFromResultSet(ResultSet source, QueryResult destination) throws SQLException {
        ResultSetMetaData metaData = source.getMetaData();
        int columns = metaData.getColumnCount();
        List<String> header = new ArrayList<>();
        for (int i = 1; i <= columns; i++) {
            String columnName = metaData.getColumnName(i);

            if (header.contains(columnName) || columnName.isEmpty()) {
                if (columnName.isEmpty()) {
                    columnName = "C";
                }
                for (int j = 1; j <= columns; j++) {
                    if (!header.contains(columnName + j)) {
                        columnName = columnName + j;
                        break;
                    }
                }
            }
            header.add(columnName);

        }

        destination.setHeader(header);

        while (source.next()) {
            HashMap<String, Object> row = new HashMap<>();
            for(int i = 1; i<=columns; i++){
                String columnName = header.get(i - 1);
                Object value = source.getObject(i);
                row.put(columnName, value == null ? "" : value);
            }
            destination.addValues(row);
        }

        return destination;
    }
}

