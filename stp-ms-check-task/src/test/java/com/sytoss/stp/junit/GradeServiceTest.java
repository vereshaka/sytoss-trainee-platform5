package com.sytoss.stp.junit;

import com.sytoss.stp.service.GradeService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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