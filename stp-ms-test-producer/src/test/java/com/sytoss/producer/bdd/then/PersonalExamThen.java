package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersonalExamThen extends CucumberIntegrationTest {

    @DataTableType
    public Answer mapAnswer(Map<String, String> entry) {
        Answer answer = new Answer();
        Task taskOne = new Task();
        taskOne.setQuestion(entry.get("task"));
        answer.setTask(taskOne);
        return answer;
    }

    @Given("^\"(.*)\" exam by \"(.*)\" discipline and \"(.*)\" topic for student with (.*) id should have tasks$")
    public void thisCustomerHasProjects(String examName, String disciplineName, String topic, String studentId, List<Answer> answers) throws JsonProcessingException {
        PersonalExam personalExamAnswer = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(disciplineName, personalExamAnswer.getDiscipline().getName());
        assertEquals(examName, personalExamAnswer.getName());
        assertEquals(PersonalExamStatus.NOT_STARTED, personalExamAnswer.getStatus());
        assertEquals(Long.getLong(studentId), personalExamAnswer.getStudentId());
        int quantityOfTasks = 0;
        for (Answer answer : answers) {
            for (Answer answerFromResponse : personalExamAnswer.getAnswers())
                if (answer.getTask().getQuestion().equals(answerFromResponse.getTask().getQuestion())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, personalExamAnswer.getAnswers().size());
    }
}
