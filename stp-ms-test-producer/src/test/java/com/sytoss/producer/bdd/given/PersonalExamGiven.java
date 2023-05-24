package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PersonalExamGiven extends CucumberIntegrationTest {

    @Given("tasks exist")
    public void thisCustomerHasProjects(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        //TODO dmitriyK: need to rewrite when metadata microservice would be exist
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

    @Given("^personal \"(.*)\" exam for student with (.*) id and (.*) status exist$")
    public void personalExamExist(String examName, String studentId, String answerStatus, List<Answer> answers) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(examName, Long.parseLong(studentId));
        if (personalExam != null) {
            personalExam.getAnswers().clear();
            getPersonalExamConnector().save(personalExam);
        } else {
            personalExam = new PersonalExam();
            personalExam.setName(examName);
            personalExam.setStudentId(Long.parseLong(studentId));
            personalExam.setStatus(PersonalExamStatus.valueOf(answerStatus));
        }
        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);
    }
}