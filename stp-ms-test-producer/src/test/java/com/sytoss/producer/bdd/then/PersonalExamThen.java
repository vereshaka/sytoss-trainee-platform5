package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersonalExamThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" exam by \"(.*)\" discipline and \"(.*)\" topic for student with (.*) id should have tasks$")
    public void thisCustomerHasProjects(String examName, String disciplineName, String topic, Long studentId, List<Answer> answers) throws JsonProcessingException {
        PersonalExam personalExamAnswer = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(disciplineName, personalExamAnswer.getDiscipline().getName());
        assertEquals(examName, personalExamAnswer.getName());
        assertEquals(PersonalExamStatus.NOT_STARTED, personalExamAnswer.getStatus());
        assertEquals(studentId, personalExamAnswer.getStudentId());
        int quantityOfTasks = 0;
        for (Answer answer : answers) {
            for (Answer answerFromResponse : personalExamAnswer.getAnswers())
                if (answer.getTask().getQuestion().equals(answerFromResponse.getTask().getQuestion())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, personalExamAnswer.getAnswers().size());
    }

    @Then("summary grade should be {float}")
    public void summaryGrade(Float summaryGrade) throws JsonProcessingException {
        PersonalExam personalExam = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
        assertEquals(summaryGrade, personalExam.getSummaryGrade());
    }

    @Then("response should return personal exam with exam name {string} and studentID {long} and date {word}")
    public void responseShouldReturnPersonalExam(String examName, Long studentId, String date, List<Answer> answers) throws JsonProcessingException, ParseException {
        PersonalExam personalExam = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);

        assertEquals(examName, personalExam.getName());
        assertEquals(studentId, personalExam.getStudentId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        assertEquals(dateFormat.parse(date), personalExam.getDate());

        List<Answer> listAnswer = personalExam.getAnswers();
        listAnswer.forEach(answer -> {
            List<Answer> foundAnswers = answers.stream().filter(item ->
                    Objects.equals(answer.getValue(), item.getValue()) &&
                            Objects.equals(answer.getTask().getQuestion(), item.getTask().getQuestion()) &&
                            answer.getStatus().equals(item.getStatus()) &&
                            answer.getGrade().getValue() == item.getGrade().getValue() &&
                            Objects.equals(answer.getGrade().getComment(), item.getGrade().getComment())
            ).collect(Collectors.toList());

            answers.remove(foundAnswers.get(0));
        });

        assertEquals(0, answers.size());
    }

    @Then("^status of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void personalExamShouldHaveStatus(String examName, Long studentId, String personalExamStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(examName, studentId);
        assertEquals(personalExamStatus, personalExam.getStatus().toString());
    }

    @Then("should return personal exam with time {int} and amountOfTasks {long}")
    public void shouldReturnPersonalExamWithTimeAndAmountOfTasks(int time, Long amountOfTasks){
        FirstTask firstTask = IntegrationTest.getTestContext().getFirstTaskResponse().getBody();
        assertEquals(time, firstTask.getExam().getTime());
        assertEquals(Integer.valueOf(Math.toIntExact(amountOfTasks)), firstTask.getExam().getAmountOfTasks());
    }
}
