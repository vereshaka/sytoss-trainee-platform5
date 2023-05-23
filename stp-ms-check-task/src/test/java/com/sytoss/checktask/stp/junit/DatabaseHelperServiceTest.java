package com.sytoss.checktask.stp.junit;

import com.sytoss.checktask.stp.service.DatabaseHelperService;
import com.sytoss.stp.junit.DatabaseInitHelper;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class DatabaseHelperServiceTest extends DatabaseInitHelper {

    private final DatabaseHelperService databaseHelperService = mock(DatabaseHelperService.class);


    @Test
    void generateDatabase() throws Exception {
        String script = initDatabase();
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