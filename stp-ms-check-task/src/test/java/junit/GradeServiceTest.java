package junit;

import com.sytoss.stp.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceTest extends DatabaseInitHelper {

    private final GradeService gradeService = mock(GradeService.class);
    @Test
    void checkAndGrade() throws Exception {
        String script = initDatabase();
        gradeService.checkAndGrade("select all from Person",
                "select * from Person",
                script);
        verify(gradeService).checkAndGrade("select all from Person",
                "select * from Person",
                script);
    }
}