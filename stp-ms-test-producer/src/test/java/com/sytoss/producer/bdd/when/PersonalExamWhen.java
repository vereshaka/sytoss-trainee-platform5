package com.sytoss.producer.bdd.when;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.bom.Task;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

@Slf4j
public class PersonalExamWhen extends CucumberIntegrationTest {

    @When("^student with (.*) id start personal exam \"(.*)\"$")
    public void requestSentStartPersonalExam(String studentId, String personalExamName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Task> requestEntity = new HttpEntity<>(null, headers);
        PersonalExam input = getPersonalExamConnector().getByNameAndStudentId(personalExamName, Long.parseLong(studentId));
        String url = getBaseUrl() + "/api/test/" + input.getId() + "/start";
        log.info("Send request to " + url);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}