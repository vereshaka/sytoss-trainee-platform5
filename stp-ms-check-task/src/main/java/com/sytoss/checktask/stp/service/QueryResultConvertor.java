package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.QueryResult;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class QueryResultConvertor {

    public String convertFromResultSet(ResultSet resultSet) throws SQLException {
        String answer = "";
        while (resultSet.next()) {
            answer = resultSet.getString(1);
        }
        return answer;
    }
}

