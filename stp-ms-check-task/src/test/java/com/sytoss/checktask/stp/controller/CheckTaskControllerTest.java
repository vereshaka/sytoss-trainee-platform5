package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.exceptions.RequestIsNotValidException;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.checktask.stp.service.ScoreService;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sytoss.stp.test.FileUtils.readFromFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CheckTaskControllerTest extends StpApplicationTest {

    @MockBean
    private ScoreService scoreService;

    @InjectMocks
    private CheckTaskController checkTaskController;

    @Test
    public void checkAndGradeWithWrongEtalon() {
        when(scoreService.checkAndScore(any())).thenThrow(new WrongEtalonException("", new RuntimeException()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Authors");
        checkTaskParameters.setEtalon("select  Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        RequestEntity httpEntity = new RequestEntity(checkTaskParameters, null, null);

        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);

        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }


    @Test
    void checkAndGradeWithWrongScript() {
        when(scoreService.checkAndScore(any())).thenThrow(new DatabaseCommunicationException("", new RuntimeException()));

        CheckTaskParameters checkAnswerRequestBody = new CheckTaskParameters();
        checkAnswerRequestBody.setRequest("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{: [{changeSet: {author {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody, null, null);

        ResponseEntity<Score> responseEntity = doPost("/api/task/check", httpEntity, Score.class);

        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

    @Test
    void shouldCheckRequest() {
        List<HashMap<String, Object>> resultMapList = new ArrayList<>();
        when(scoreService.checkRequest(any())).thenReturn(new QueryResult(resultMapList));

        CheckRequestParameters checkRequestBody = new CheckRequestParameters();
        checkRequestBody.setRequest("select * from Authors");
        checkRequestBody.setScript(readFromFile("script1.json"));
        RequestEntity httpEntity = new RequestEntity(checkRequestBody, null, null);

        ResponseEntity<Score> responseEntity = doPost("/api/task/check-request", httpEntity, Score.class);

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void shouldCheckRequestNotValid() {
        when(scoreService.checkRequest(any())).thenThrow(new RequestIsNotValidException("", new RuntimeException()));

        CheckRequestParameters checkRequestBody = new CheckRequestParameters();
        checkRequestBody.setRequest("select * from asdfasdfasdf");
        checkRequestBody.setScript(readFromFile("script1.json"));
        RequestEntity httpEntity = new RequestEntity(checkRequestBody, null, null);

        ResponseEntity<Score> responseEntity = doPost("/api/task/check-request", httpEntity, Score.class);

        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }
}
