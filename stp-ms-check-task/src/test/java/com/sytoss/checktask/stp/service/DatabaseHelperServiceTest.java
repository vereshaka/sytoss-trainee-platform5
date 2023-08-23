package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.StpUnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

@Slf4j
class DatabaseHelperServiceTest extends StpUnitTest {

    private final DatabaseHelperService databaseHelperService = new DatabaseHelperService(new QueryResultConvertor());

    @Test
    void generateDatabase() {
        databaseHelperService.generateDatabase(FileUtils.readFromFile("script1.yml"));
        Assertions.assertDoesNotThrow(() -> databaseHelperService.getExecuteQueryResult("select * from discipline"));
    }

    @Test
    public void generateDatabase_puml() {
        String pumlv = FileUtils.readFromFile("script_v1.puml");
        String pumlScript = new PumlConvertor().formatPuml(pumlv);
        String script = new PumlConvertor().convertToLiquibase(pumlScript);
        log.info(script);
        databaseHelperService.generateDatabase(script);
        Assertions.assertDoesNotThrow(() -> databaseHelperService.getExecuteQueryResult("select * from discipline"));
    }

    @Test
    public void generateDatabase_v1() {
        String script = FileUtils.readFromFile("script_v1.yml");
        databaseHelperService.generateDatabase(script);
        //Assertions.assertDoesNotThrow(() -> databaseHelperService.getExecuteQueryResult("select * from discipline"));
    }

    @Test
    void getExecuteQueryResult() throws SQLException {
        databaseHelperService.generateDatabase(FileUtils.readFromFile("script1.yml"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("ID", 1L);
        map.put("NAME", "SQL");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("ID", 2L);
        map2.put("NAME", "Mongo");
        QueryResult queryResult = new QueryResult(List.of(map,map2));
        int quantityOfInitiatedElements = map.size() + map2.size();

        QueryResult queryResultFromDatabase = databaseHelperService.getExecuteQueryResult("select * from discipline");
        int quantityOfElements = 0;
        for (int i = 0; i < queryResultFromDatabase.getResultMapList().size(); i++) {
            List<String> keys = queryResultFromDatabase.getRow(i).keySet().stream().toList();
            for (String key : keys) {
                if (Objects.equals(queryResultFromDatabase.getRow(i).get(key), queryResult.getRow(i).get(key))) {
                    quantityOfElements++;
                }
            }
        }
        Assertions.assertEquals(quantityOfInitiatedElements,quantityOfElements);
    }

    @Test
    void dropDatabase() {
        databaseHelperService.generateDatabase(FileUtils.readFromFile("script1.yml"));
        databaseHelperService.dropDatabase();
        Assertions.assertThrows(CreateDbConnectionException.class, () -> databaseHelperService.getExecuteQueryResult("select * from answer"));
    }
}
