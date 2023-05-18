package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.*;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersonalExamThen extends CucumberIntegrationTest {

    @DataTableType
    public PersonalExam mapPersonalExam(Map<String, String> entry) {
        PersonalExam personalExam = new PersonalExam();
        Answer answerFirst = new Answer();
        Answer answerSecond = new Answer();
        Discipline discipline = new Discipline();
        Task taskOne = new Task();
        Task taskSecond = new Task();
        discipline.setName(entry.get("discipline"));
        taskOne.setQuestion(entry.get("task one"));
        taskSecond.setQuestion(entry.get("task two"));
        answerFirst.setTask(taskOne);
        answerSecond.setTask(taskSecond);
        personalExam.setDiscipline(discipline);
        personalExam.setAnswers(List.of(answerFirst, answerSecond));
        return personalExam;
    }

    @Given("^\"(.*)\" exam for student with (.*) id should have tasks$")
    public void thisCustomerHasProjects(String examName, String studentId, List<PersonalExam> personalExams) throws JsonProcessingException {
        PersonalExam personalExamAnswer = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(examName, personalExamAnswer.getName());
        assertEquals(PersonalExamStatus.NOT_STARTED, personalExamAnswer.getStatus());
        assertEquals(Long.getLong(studentId), personalExamAnswer.getStudentId());
        int quantityOfTasks = 0;
        for (PersonalExam personalExam : personalExams) {
            for (Answer answer : personalExam.getAnswers()) {
                for (Answer answerFromResponse : personalExamAnswer.getAnswers())
                    if (answer.getTask().getQuestion().equals(answerFromResponse.getTask().getQuestion())) {
                        quantityOfTasks++;
                    }
            }
            assertEquals(quantityOfTasks, personalExamAnswer.getAnswers().size());
        }
    }

}
