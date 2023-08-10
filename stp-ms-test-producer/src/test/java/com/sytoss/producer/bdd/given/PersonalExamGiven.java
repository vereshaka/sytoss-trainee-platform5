package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PersonalExamGiven extends TestProducerIntegrationTest {

    @Given("tasks exist")
    public void thisCustomerHasProjects(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();

        for (Map<String, String> columns : rows) {
            if (!(getTestExecutionContext().getDetails().getTaskMapping().containsKey(columns.get("topic")))) {
                getTestExecutionContext().getDetails().getTaskMapping().put(columns.get("topic"), columns.get("task"));
            } else {
                String tasks = getTestExecutionContext().getDetails().getTaskMapping().get(columns.get("topic"));
                tasks = tasks + ", " + columns.get("task");
                getTestExecutionContext().getDetails().getTaskMapping().replace(columns.get("topic"), tasks);
            }
        }
    }

    @Given("^personal exam exists with id (.*) and exam name (.*) and studentID (.*) and date (.*)")
    public void thisExamHasAnswers(String examId, String examName, String studentId, String date, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId(examId);
        personalExam.setName(examName);
        Student student = new Student();
        student.setUid(studentId);
        personalExam.setStudent(student);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            personalExam.setAssignedDate(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);
    }

    @Given("^personal \"(.*)\" exam(?: with examId (.*)|) for student with (.*) id and (.*) status exist and time (.*) and amountOfTasks (.*)$")
    public void personalExamExist(String examName, String examId, String studentId, String answerStatus, String time, String amountOfTasks, List<Answer> answers) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentId);
        if (personalExam != null) {
            personalExam.getAnswers().clear();
            getPersonalExamConnector().save(personalExam);
        } else {
            personalExam = new PersonalExam();
            personalExam.setName(examName);
            Student student = new Student();
            student.setUid(studentId);
            student.setId(Long.valueOf(studentId));
            personalExam.setStudent(student);
            personalExam.setTime(Integer.valueOf(time));
            personalExam.setAmountOfTasks(Integer.valueOf(amountOfTasks));
            personalExam.setStatus(PersonalExamStatus.valueOf(answerStatus));
            if(StringUtils.isNotEmpty(examId)) {
                personalExam.setExamId(Long.valueOf(examId));
            }
        }
        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);
    }
}