package com.sytoss.checktask.stp.junit;

import com.sytoss.checktask.stp.service.DatabaseHelperService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class DatabaseHelperServiceTest {

    private final DatabaseHelperService databaseHelperService = mock(DatabaseHelperService.class);

    @Test
    void generateDatabase() {
        String script = "{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}";
        databaseHelperService.generateDatabase(script);
        verify(databaseHelperService).generateDatabase(script);
    }

    @Test
    void executeQuery() throws Exception {
        databaseHelperService.executeQuery("select * from answer");
        verify(databaseHelperService).executeQuery("select * from answer");
    }

    @Test
    void dropDatabase() throws Exception {
        databaseHelperService.dropDatabase();
        verify(databaseHelperService, times(1)).dropDatabase();
    }
}