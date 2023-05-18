package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.bom.Answer;
import com.sytoss.producer.bom.AnswerStatus;
import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.bom.PersonalExamStatus;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersonalExamThen extends CucumberIntegrationTest {

    @Given("^\"(.*)\" exam for sudent with 1L id should have tasks$")
    public void thisCustomerHasProjects(String examName, DataTable table) throws JsonProcessingException {
        List<Map<String, String>> rows = table.asMaps();
        //TODO dmitriyK: need to rewrite when metadata microservice would be exist
        PersonalExam personalExam = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(examName, personalExam.getName());
        assertEquals(PersonalExamStatus.NOT_STARTED, personalExam.getStatus());
        List<Answer> answers = personalExam.getAnswers();
        int quantityOfTasks = 0;
        for (Map<String, String> column : rows) {
            for (int i = 0; i < answers.size(); i++) {
                if (answers.get(i).getTask().getQuestion().equals(column.get("task"))) {
                    quantityOfTasks++;
                    assertEquals(AnswerStatus.NOT_STARTED, answers.get(i).getStatus());
                }
            }
        }
        assertEquals(rows.size(), quantityOfTasks);
    }
}
