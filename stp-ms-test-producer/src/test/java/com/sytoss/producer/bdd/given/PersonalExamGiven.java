package com.sytoss.producer.bdd.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bom.*;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PersonalExamGiven extends CucumberIntegrationTest {

    @DataTableType
    public Answer mapAnswer(Map<String, String> entry) {
        Topic topic = new Topic();
        topic.setName(entry.get("listOfSubjects"));

        Task task = new Task();
        task.setId(Long.parseLong(entry.get("taskId")));
        task.setQuestion(entry.get("question"));
        task.setTopics(List.of(topic));

        Grade grade = new Grade();
        grade.setValue(Float.parseFloat(entry.get("grade")));
        grade.setComment(entry.get("comment"));

        Answer answer = new Answer();
        answer.setTask(task);
        answer.setValue(entry.get("answer"));
        answer.setGrade(grade);
        answer.setStatus(AnswerStatus.valueOf(entry.get("status")));

        return answer;
    }

    @Given("personal exam exists with id {word} and exam name {string} and studentID {long} and date {word}")
    public void thisExamHasAnswers(String examId, String examName, Long studentId, String date, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(examId);
        personalExam.setName(examName);
        personalExam.setStudentId(studentId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            personalExam.setDate(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        personalExam.setAnswers(answers);

        getPersonalExamConnector().save(personalExam);
    }
}
