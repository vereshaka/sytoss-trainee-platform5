package com.sytoss.stp.service;

import bom.QueryResult;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
@Component
public class QueryResultConvertor {

   // private QueryResult queryResult = new QueryResult();


    public QueryResult convert(ResultSet resultSet, String parameter,QueryResult queryResult) throws SQLException {
        while(resultSet.next()){
            String answer = resultSet.getString(1);
            if (Objects.equals(parameter, "answer")) {
                queryResult.setAnswer(answer);
            } else {
                queryResult.setEtalon(answer);
            }
            //resultSet.close();
            System.out.println(queryResult);
        }
        return queryResult;
    }
}

