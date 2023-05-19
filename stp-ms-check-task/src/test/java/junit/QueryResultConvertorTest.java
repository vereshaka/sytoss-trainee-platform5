package junit;

import bom.QueryResult;
import com.sytoss.stp.service.DatabaseHelperService;
import com.sytoss.stp.service.QueryResultConvertor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class QueryResultConvertorTest {
    private final DatabaseHelperService databaseHelperService;
    @Mock
    private final QueryResultConvertor queryResultConvertor;
    @Mock
    private ResultSet resultSet;
    @Test
    public void getExecuteQueryResult() throws Exception {
        String answer = "select all from Person";
        String etalon = "select * from Person";
        when(resultSet.getString("answer")).thenReturn(answer);
        when(resultSet.getString("etalon")).thenReturn(answer);
        //Connection connection = databaseHelperService.generateDatabase("stp-ms-check-task/src/test/resources/databaseTest.yml");
        QueryResult queryResult = new QueryResult();
  //      verify(queryResultConvertor,times(1)).getExecuteQueryResult(connection);
        assertEquals(queryResult.getAnswer(),answer);
        assertEquals(queryResult.getEtalon(),etalon);
    }
}