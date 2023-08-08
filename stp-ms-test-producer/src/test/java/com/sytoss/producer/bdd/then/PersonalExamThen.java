package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PersonalExamThen extends TestProducerIntegrationTest {

    @Then("^\"(.*)\" exam by \"(.*)\" discipline and \"(.*)\" topic for student with (.*) id should have tasks$")
    public void thisCustomerHasProjects(String examName, String disciplineName, String topic, String studentId, List<Answer> answers) throws JsonProcessingException {
        PersonalExam personalExamAnswer = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();
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

    @Then("^summary grade should be (.*)$")
    public void summaryGrade(Float summaryGrade) throws JsonProcessingException {
        PersonalExam personalExam = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();
        assertEquals(summaryGrade, personalExam.getSummaryGrade());
    }

    @Then("^response should return personal exam with exam name (.*) and studentID (.*) and date (.*)$")
    public void responseShouldReturnPersonalExam(String examName, String studentId, String date, List<Answer> answers) throws JsonProcessingException, ParseException {
        PersonalExam personalExam = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();

        assertEquals(examName, personalExam.getName());
        assertEquals(studentId, personalExam.getStudent().getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        assertEquals(dateFormat.parse(date), personalExam.getAssignedDate());

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
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentId);
        assertEquals(personalExamStatus, personalExam.getStatus().toString());
    }

    @Then("^should return personal exam with time (.*) and amountOfTasks (.*)$")
    public void shouldReturnPersonalExamWithTimeAndAmountOfTasks(int time, Long amountOfTasks) {
        Question firstTask = getTestExecutionContext().getDetails().getFirstTaskResponse().getBody();
        assertEquals(time, firstTask.getExam().getTime());
        assertEquals(Integer.valueOf(Math.toIntExact(amountOfTasks)), firstTask.getExam().getAmountOfTasks());
    }

    @Then("^should return \"(.*)\"$")
    public void shouldReturnIsTaskDomainUsed(String isUsed) {
        assertEquals(Boolean.valueOf(isUsed), Boolean.valueOf(getTestExecutionContext().getDetails().getResponse().getBody()));
    }

    @Then("tasks from exam should have id image")
    public void tasksFromExamShouldHaveIdImage() throws JsonProcessingException {
        PersonalExam personalExamAnswer = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();
        int quantityOfImages = 0;
        List<Task> tasks = personalExamAnswer.getAnswers().stream().map(Answer::getTask).toList();
        for (Task task : tasks) {
            if (task.getImageId() != null) {
                quantityOfImages++;
            }
        }
        assertEquals(tasks.size(), quantityOfImages);
    }

    @Then("operation should return {} personal exams")
    public void operationShouldReturnPersonalExams(int countPersonalExam, DataTable dataTable) throws JsonProcessingException {
        List<PersonalExam> personalExams = getMapper().readValue(getTestExecutionContext().getDetails().getResponse().getBody(), new TypeReference<>() {
        });
        assertEquals(countPersonalExam, personalExams.size());

        List<String> examNames = personalExams.stream().map(PersonalExam::getName).toList();
        List<String> compareWithExamNames = dataTable.asList();

        if(!examNames.containsAll(compareWithExamNames)){
            fail("Invalid exam names were retrieved");
        }
    }
}
