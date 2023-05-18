package com.sytoss.producer.bdd.given;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bom.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalExamGiven extends CucumberIntegrationTest {

    @Given("^personal exam exist$")
    public void personalExamExist(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(rows.get(0).get("exam name"), Long.parseLong(rows.get(0).get("studentId")));
        if (personalExam != null) {
            personalExam.getAnswers().clear();
            getPersonalExamConnector().save(personalExam);
        } else {
            personalExam = new PersonalExam();
            personalExam.setName(rows.get(0).get("exam name"));
            personalExam.setStudentId(Long.parseLong(rows.get(0).get("studentId")));
            personalExam.setStatus(PersonalExamStatus.valueOf(rows.get(0).get("exam status")));
        }
        List<Answer> answerList = new ArrayList<>();
        for (Map<String, String> columns : rows) {
            Task task = new Task();
            task.setQuestion(columns.get("question"));
            Answer answer = new Answer();
            answer.setTask(task);
            answer.setStatus(AnswerStatus.valueOf(columns.get("task status")));
            answerList.add(answer);
        }
        personalExam.setAnswers(answerList);
        getPersonalExamConnector().save(personalExam);
    }
}

