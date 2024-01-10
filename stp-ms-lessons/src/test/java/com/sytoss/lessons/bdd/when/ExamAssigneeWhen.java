package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class ExamAssigneeWhen extends LessonsIntegrationTest {
    @When("^delete exam assignee with id (.*)")
    public void deleteById(String examAssigneeId) {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ExamAssignee> response = doDelete("/api/assignee/delete/"
                + getTestExecutionContext().replaceId(examAssigneeId),
                httpEntity, ExamAssignee.class);
        getTestExecutionContext().setResponse(response);
    }
}
