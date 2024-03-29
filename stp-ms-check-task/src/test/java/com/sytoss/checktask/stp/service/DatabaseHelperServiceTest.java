package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.service.db.H2Executor;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.StpUnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
class DatabaseHelperServiceTest extends StpUnitTest {

    @Test
    void generateDatabase() {
        DatabaseHelperService databaseHelperService = new DatabaseHelperService(new QueryResultConvertor(), new H2Executor());
        databaseHelperService.generateDatabase(FileUtils.readFromFile("task-domain/script_v1.yml"));
        Assertions.assertDoesNotThrow(() -> databaseHelperService.getExecuteQueryResult("select * from Client",null));
    }

    @Test
    void getExecuteQueryResult() throws SQLException {
        DatabaseHelperService databaseHelperService = new DatabaseHelperService(new QueryResultConvertor(), new H2Executor());
        databaseHelperService.generateDatabase(FileUtils.readFromFile("task-domain/script1.yml"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("ID", 1L);
        map.put("NAME", "SQL");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ID", 2L);
        map2.put("NAME", "Mongo");
        QueryResult queryResult = new QueryResult();
        queryResult.setHeader(List.of("ID", "NAME"));
        queryResult.addValues(map);
        queryResult.addValues(map2);
        int quantityOfInitiatedElements = map.size() + map2.size();

        QueryResult queryResultFromDatabase = databaseHelperService.getExecuteQueryResult("select * from discipline",null);
        int quantityOfElements = 0;
        List<String> keys = queryResultFromDatabase.getHeader();
        for (int i = 0; i < queryResultFromDatabase.getResultMapList().size(); i++) {
            for (String key : keys) {
                if (Objects.equals(queryResultFromDatabase.getValue(i, key), queryResult.getValue(i, key))) {
                    quantityOfElements++;
                }
            }
        }
        Assertions.assertEquals(quantityOfInitiatedElements, quantityOfElements);
    }

    @Test
    void dropDatabase() {
        DatabaseHelperService databaseHelperService = new DatabaseHelperService(new QueryResultConvertor(), new H2Executor());
        databaseHelperService.generateDatabase(FileUtils.readFromFile("task-domain/script1.yml"));
        databaseHelperService.dropDatabase();
        Assertions.assertThrows(SQLException.class, () -> databaseHelperService.getExecuteQueryResult("select * from discipline",null));
    }
}
