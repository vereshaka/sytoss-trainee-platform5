package junit;

import com.sytoss.stp.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private final GradeService gradeService = mock(GradeService.class);
    @Test
    void checkAndGrade() throws Exception {
        verify(gradeService,times(1)).checkAndGrade("select all from Person",
                "select * from Person",
                "stp-ms-check-task/src/test/resources/databaseTest.yml");
    }
}