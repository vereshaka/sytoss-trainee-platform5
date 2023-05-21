package junit;

import com.sytoss.stp.service.DatabaseHelperService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DatabaseHelperServiceTest extends DatabaseInitHelper{

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
        verify(databaseHelperService,times(1)).dropDatabase();
    }
}