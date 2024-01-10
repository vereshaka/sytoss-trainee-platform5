package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeToDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ExamAssigneeThen extends LessonsIntegrationTest {

    @Then("^there is no exam assignee with id (.*)$")
    public void thereIsNoExamAssigneeWithId(String examAssigneeId) {
        Assertions.assertFalse(getExamAssigneeConnector().findById((Long)getTestExecutionContext().replaceId(examAssigneeId)).isPresent());

        List<ExamAssigneeToDTO> examAssigneeToDTOS = getExamAssigneeToConnector()
                .getAllByParent_IdOrderByParent_RelevantFromDesc((Long)getTestExecutionContext().replaceId(examAssigneeId));
        Assertions.assertEquals(0, examAssigneeToDTOS.size());
    }
}
