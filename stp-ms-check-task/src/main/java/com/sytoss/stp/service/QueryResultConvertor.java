package com.sytoss.stp.service;

import bom.QueryResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class QueryResultConvertor {

    private QueryResult queryResult = new QueryResult();

    public QueryResult getExecuteQueryResult(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from answer");
        convert(resultSet, "answer");
        resultSet = statement.executeQuery("select * from etalon");
        convert(resultSet, "etalon");
        return queryResult;
    }

    public void convert(ResultSet resultSet, String parameter) throws SQLException {
        String answer = resultSet.getString(parameter);
        if (Objects.equals(parameter, "answer")) {
            queryResult.setAnswer(answer);
        } else {
            queryResult.setEtalon("etalon");
        }
        resultSet.close();
    }
}

