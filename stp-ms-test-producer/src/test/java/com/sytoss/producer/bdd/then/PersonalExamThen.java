package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Task;
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

    @DataTableType
    public Answer mapAnswer(Map<String, String> entry) {
        Answer answer = new Answer();
        Task taskOne = new Task();

        if (entry.containsKey("task")) {
            taskOne.setQuestion(entry.get("task"));
            answer.setTask(taskOne);
        }

        Topic topic = new Topic();
        if (entry.containsKey("listOfSubjects")) {
            topic.setName(entry.get("listOfSubjects"));
        }
        Task task = new Task();

        if (entry.containsKey("taskId")) {
            task.setId(Long.parseLong(entry.get("taskId")));
        }
        if (entry.containsKey("question")) {
            task.setQuestion(entry.get("question"));
            task.setTopics(List.of(topic));
            answer.setTask(task);
        }

        Grade grade = new Grade();

        if (entry.containsKey("grade")) {
            grade.setValue(Float.parseFloat(entry.get("grade")));
            grade.setComment(entry.get("comment"));
            answer.setValue(entry.get("answer"));
            answer.setGrade(grade);
        }

        if (entry.containsKey("task status")) {
            answer.setStatus(AnswerStatus.valueOf(entry.get("task status")));
        }
        return answer;
    }

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

    @And("^should return personal exam with time (.*) and amountOfTasks (.*)$")
    public void shouldReturnPersonalExamWithTimeAndAmountOfTasks(String time, Long amountOfTasks) throws JsonProcessingException {
        FirstTask firstTask =  getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), FirstTask.class);
        assertEquals(Integer.valueOf(time), firstTask.getTime());
        assertEquals(Integer.valueOf(Math.toIntExact(amountOfTasks)), firstTask.getAmountOfTasks());
    }
}
