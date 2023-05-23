package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.QueryResult;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class QueryResultConvertor {

    public void convert(ResultSet resultSet, String parameter, QueryResult queryResult) throws SQLException {
        while (resultSet.next()) {
            String answer = resultSet.getString(1);
            if (Objects.equals(parameter, "answer")) {
                queryResult.setAnswer(answer);
            } else {
                queryResult.setEtalon(answer);
            }
            System.out.println(queryResult);
        }
    }
}

