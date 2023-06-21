package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.AbstractApplicationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.Grade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;


class GradeServiceTest extends AbstractApplicationTest {

    @Test
    void checkAndGradeCorrectAnswer() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(objectProvider);
        Grade grade = gradeService.checkAndGrade(checkTaskParameters);
        Assertions.assertEquals(1, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsOrder() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select id,name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(objectProvider);
        Grade grade = gradeService.checkAndGrade(checkTaskParameters);
        Assertions.assertEquals(1, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    @Test
    void checkAndGradeWrongAnswer() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Books");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(objectProvider);
        Grade grade = gradeService.checkAndGrade(checkTaskParameters);
        Assertions.assertEquals(0, grade.getValue());
        Assertions.assertEquals("not ok", grade.getComment());
    }

    @Test
    void checkAndGradeWithWrongEtalon() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select  Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        RequestEntity httpEntity = new RequestEntity(checkTaskParameters, null, null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }

    @Test
    void checkAndGradeWithWrongScript() {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript("Wrong script");
        RequestEntity httpEntity = new RequestEntity(checkTaskParameters, null, null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

    private String readFromFile(String script) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("scripts/" + script).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        return String.join("\n", data);
    }
}