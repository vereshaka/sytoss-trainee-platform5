package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PersonalExamThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" exam by \"(.*)\" discipline and \"(.*)\" topic for student with (.*) id should have tasks$")
    public void thisCustomerHasProjects(String examName, String disciplineName, String topic, String studentId, List<Answer> answers) throws JsonProcessingException {
        PersonalExam personalExamAnswer = IntegrationTest.getTestContext().getPersonalExamResponse().getBody();
        assertEquals(disciplineName, personalExamAnswer.getDiscipline().getName());
        assertEquals(examName, personalExamAnswer.getName());
        assertEquals(PersonalExamStatus.NOT_STARTED, personalExamAnswer.getStatus());
        assertEquals(studentId, personalExamAnswer.getStudent().getUid());
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
        PersonalExam personalExam = IntegrationTest.getTestContext().getPersonalExamResponse().getBody();
        assertEquals(summaryGrade, personalExam.getSummaryGrade());
    }

    @Then("^response should return personal exam with exam name (.*) and studentID (.*) and date (.*)")
    public void responseShouldReturnPersonalExam(String examName, String studentId, String date, List<Answer> answers) throws JsonProcessingException, ParseException {
        PersonalExam personalExam = IntegrationTest.getTestContext().getPersonalExamResponse().getBody();

        assertEquals(examName, personalExam.getName());
        assertEquals(studentId, personalExam.getStudent().getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        assertEquals(dateFormat.parse(date), personalExam.getDate());

        List<Answer> listAnswer = personalExam.getAnswers();
        Iterator<Answer> i = listAnswer.iterator();

        while (i.hasNext()) {
            for (Answer answer : answers) {
                Answer answerResult = i.next();
                if (answer.getValue().equals(answerResult.getValue()) &&
                        answer.getStatus().equals(answerResult.getStatus()) &&
                        answer.getGrade().getValue() == answerResult.getGrade().getValue() &&
                        answer.getGrade().getComment().equals(answerResult.getGrade().getComment())) {
                    i.remove();
                }
            }
        }
        assertEquals(0, listAnswer.size());
    }

    @Then("^status of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void personalExamShouldHaveStatus(String examName, String studentId, String personalExamStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudent_Uid(examName, studentId);
        assertEquals(personalExamStatus, personalExam.getStatus().toString());
    }

    @Then("should return personal exam with time {int} and amountOfTasks {long}")
    public void shouldReturnPersonalExamWithTimeAndAmountOfTasks(int time, Long amountOfTasks) {
        Question firstTask = IntegrationTest.getTestContext().getFirstTaskResponse().getBody();
        assertEquals(time, firstTask.getExam().getTime());
        assertEquals(Integer.valueOf(Math.toIntExact(amountOfTasks)), firstTask.getExam().getAmountOfTasks());
    }

    @Then("^should return \"(.*)\"$")
    public void shouldReturnIsTaskDomainUsed(String isUsed) {
        assertEquals(Boolean.valueOf(isUsed), Boolean.valueOf(IntegrationTest.getTestContext().getResponse().getBody()));
    }

    @Then("tasks from exam should have id image")
    public void tasksFromExamShouldHaveIdImage() throws JsonProcessingException {
        PersonalExam personalExamAnswer = IntegrationTest.getTestContext().getPersonalExamResponse().getBody();
        int quantityOfImages = 0;
        List<Task> tasks = personalExamAnswer.getAnswers().stream().map(Answer::getTask).toList();
        for (Task task : tasks) {
            if (task.getImageId() != null) {
                quantityOfImages++;
            }
        }
        assertEquals(tasks.size(), quantityOfImages);
    }
}
