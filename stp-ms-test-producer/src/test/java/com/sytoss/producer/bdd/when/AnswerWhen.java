package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AnswerWhen extends CucumberIntegrationTest {

    @When("^student calls answer with value \"(.*)\" on personal exam with id (.*)$")
    public void studentCallsAnswer(String answer, String personalExamId) throws JOSEException {

        Task task = new Task();
        task.setId(13L);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setDatabaseScript(".uml");
        task.setTaskDomain(taskDomain);

        when(getMetadataConnector().getTaskById(anyLong())).thenReturn(task);
        when(getMetadataConnector().getTaskDomain(anyLong())).thenReturn(taskDomain);

        Score score = new Score();
        score.setValue(1);
        when(getCheckTaskConnector().checkAnswer(any(CheckTaskParameters.class))).thenReturn(score);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123"), String.valueOf(IntegrationTest.getTestContext().getStudentId())));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "/api/personal-exam/" + personalExamId + "/task/answer";
        HttpEntity<String> request = new HttpEntity<>(answer, headers);
        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
