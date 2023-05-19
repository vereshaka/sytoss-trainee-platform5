package junit;

import com.sytoss.stp.service.DatabaseHelperService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@RequiredArgsConstructor
class DatabaseHelperServiceTest {

    @Mock
    private final DatabaseHelperService databaseHelperService;

    @Test
    void generateDatabase() throws Exception {
        verify(databaseHelperService,times(1)).generateDatabase("databaseTest.yml");
    }

    @Test
    void executeQuery() throws Exception {
    //    verify(databaseHelperService,times(1)).executeQuery("select * from answer");
    }

    @Test
    void dropDatabase() throws Exception {
   //     verify(databaseHelperService,times(1)).dropDatabase();
    }
}