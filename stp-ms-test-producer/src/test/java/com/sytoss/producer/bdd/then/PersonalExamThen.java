package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Then("^teacher grade should be (.*)$")
    public void summaryGrade(Double summaryGrade) throws JsonProcessingException {
        PersonalExam personalExam = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();
        assertEquals(summaryGrade, personalExam.getSummaryGrade());
    }

    @Then("^operation should finish with (.*) error$")
    public void operationShouldBeFinishWithError(Integer statusCode) {
        assertEquals(statusCode, getTestExecutionContext().getDetails().getStatusCode());
    }

    @Then("^response should return (.*) personal exam for student (.*) from (.*)$")
    public void responseShouldReturnPersonalExam(String examName, Long studentId, String date, List<Answer> answers) throws ParseException {
        PersonalExam personalExam = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();

        assertEquals(examName, personalExam.getName());
        assertEquals(studentId.toString(), personalExam.getStudent().getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        assertEquals(dateFormat.parse(date), personalExam.getAssignedDate());

        List<Answer> listAnswer = personalExam.getAnswers();
        Iterator<Answer> i = listAnswer.iterator();

        while (i.hasNext()) {
            for (Answer answer : answers) {
                Answer answerResult = i.next();
                if (answer.getValue().equals(answerResult.getValue()) &&
                        answer.getStatus().equals(answerResult.getStatus()) &&
                        answer.getGrade().getValue() == answerResult.getGrade().getValue()) {
                    i.remove();
                }
            }
        }
        assertEquals(0, listAnswer.size());
    }

    @Then("^status of \"(.*)\" exam for this student should be \"(.*)\"$")
    public void personalExamShouldHaveStatus(String examName, String personalExamStatus) {
        personalExamShouldHaveStatus(examName, getTestExecutionContext().getDetails().getStudentUid(), personalExamStatus);
    }


    @Then("^status of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void personalExamShouldHaveStatus(String examName, String studentId, String personalExamStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentId);
        assertEquals(personalExamStatus, personalExam.getStatus().toString());
    }

    @Then("^should return personal exam with time more than (.*) and amountOfTasks (.*)$")
    public void shouldReturnPersonalExamWithTimeAndAmountOfTasks(int time, Long amountOfTasks) {
        Question firstTask = getTestExecutionContext().getDetails().getFirstTaskResponse().getBody();
        assertTrue(time <= firstTask.getExam().getTime());
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

        if (!examNames.containsAll(compareWithExamNames)) {
            fail("Invalid exam names were retrieved");
        }
    }

    @Then("^system grade should be (.*)$")
    public void systemGrade(Double systemGrade) {
        PersonalExam personalExam = getTestExecutionContext().getDetails().getPersonalExamResponse().getBody();
        assertEquals(systemGrade, personalExam.getSystemGrade());
    }

    @Then("personal exams are")
    public void personalExamsAre(DataTable dataTable) throws ParseException {
        List<Map<String, String>> personalExamsMaps = dataTable.asMaps();
        List<PersonalExam> personalExams = new ArrayList<>();
        List<PersonalExam> personalExamsFromResponse = getTestExecutionContext().getDetails().getPersonalExams();
        for (Map<String, String> personalExamMap : personalExamsMaps) {
            PersonalExam personalExam = new PersonalExam();

            Discipline discipline = new Discipline();
            String disciplineKey = getTestExecutionContext().replaceId(personalExamMap.get("disciplineId")).toString();
            discipline.setId(Long.parseLong(disciplineKey));

            personalExam.setDiscipline(discipline);

            String examAssigneeKey = getTestExecutionContext().replaceId(personalExamMap.get("examAssigneeId")).toString();
            personalExam.setExamAssigneeId(Long.parseLong(examAssigneeKey));
            personalExam.setId(personalExamMap.get("personalExamId"));
            personalExam.setName(personalExamMap.get("personalExamId").replace("*pe",""));

            Student student = new Student();
            student.setId(Long.parseLong(personalExamMap.get("studentId")));
            personalExam.setStudent(student);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
            personalExam.setStartedDate(sdf.parse(personalExamMap.get("startDate").trim()));
            personalExam.setSystemGrade(Double.parseDouble(personalExamMap.get("summaryGrade").trim()));
            personalExams.add(personalExam);
        }

        assertEquals(personalExams.size(),personalExamsFromResponse.size());
        for (PersonalExam personalExam : personalExams) {
            for (PersonalExam personalExamFromResponse : personalExamsFromResponse) {
                assertEquals(personalExam.getDiscipline().getId(), personalExamFromResponse.getDiscipline().getId());
                assertEquals(personalExam.getExamAssigneeId(), personalExamFromResponse.getExamAssigneeId());
                assertEquals(personalExam.getStudent().getId(), personalExamFromResponse.getStudent().getId());
                assertEquals(personalExam.getId(), personalExamFromResponse.getId());
                assertEquals(personalExam.getSummaryGrade(), personalExamFromResponse.getSummaryGrade());
                assertEquals(personalExam.getStartedDate(), personalExamFromResponse.getStartedDate());
                personalExamsFromResponse.remove(personalExamFromResponse);
                break;
            }
        }
    }
}
