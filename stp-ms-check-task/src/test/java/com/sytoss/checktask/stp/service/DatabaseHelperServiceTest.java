package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

class DatabaseHelperServiceTest extends StpUnitTest {

    private static final String script = "{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}";

    private final DatabaseHelperService databaseHelperService = new DatabaseHelperService(new QueryResultConvertor());

    @Test
    void generateDatabase() {
        databaseHelperService.generateDatabase(FileUtils.readFromFile("script1.yml"));
        Assertions.assertDoesNotThrow(() -> databaseHelperService.getExecuteQueryResult("select * from discipline"));
    }

    @Test
    void getExecuteQueryResult() throws SQLException {
        databaseHelperService.generateDatabase(script);
        HashMap<String, Object> map = new HashMap<>();
        map.put("ANSWER", "it_is_answer");
        QueryResult queryResult = new QueryResult(List.of(map));
        Assertions.assertEquals(queryResult.getRow(0), databaseHelperService.getExecuteQueryResult("select * from answer").getRow(0));
    }

    @Test
    void dropDatabase() {
        databaseHelperService.generateDatabase(script);
        databaseHelperService.dropDatabase();
        Assertions.assertThrows(CreateDbConnectionException.class, () -> databaseHelperService.getExecuteQueryResult("select * from answer"));
    }
}
