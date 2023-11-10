package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Given("^this student has personal exam with id (.*) and exam name (.*) and date (.*)")
    public void thisExamHasAnswers(String examId, String examName, String date, List<Answer> answers) {
       thisExamHasAnswers(getTestExecutionContext().getDetails().getStudentId(), examId, examName, date, answers);
    }

    @Given("^student (.*) has personal exam with id (.*) and exam name (.*) and date (.*)")
    public void thisExamHasAnswers(Long studentId, String examId, String examName, String date, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId(examId);
        personalExam.setName(examName);
        Student student = new Student();
        student.setUid(String.valueOf(studentId));
        personalExam.setStudent(student);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            personalExam.setAssignedDate(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long i=0;
        for(Answer answer : answers){
            answer.setId(i++);
        }
       // answers.stream().forEach(item -> item.setTeacherGrade(item.getGrade()));

        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);

        getTestExecutionContext().getDetails().setStudentUid(student.getUid());
    }

    @Given("^this student has \"(.*)\" personal exam and (.*) status exist and time (.*) and amountOfTasks (.*)$")
    public void personalExamExist(String examName, PersonalExamStatus examStatus, String time, String amountOfTasks, List<Answer> answers) {
        String studentUid = getTestExecutionContext().getDetails().getStudentUid();
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentUid);
        if (personalExam != null) {
            personalExam.getAnswers().clear();
            getPersonalExamConnector().save(personalExam);
        } else {
            personalExam = new PersonalExam();
        }
        personalExam.setName(examName);
        personalExam.setRelevantFrom(new Date());
        personalExam.setRelevantTo(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        Student student = new Student();
        student.setUid(studentUid);
        student.setId(getTestExecutionContext().getDetails().getStudentId());
        personalExam.setStudent(student);
        personalExam.setTime(Integer.valueOf(time));
        personalExam.setAmountOfTasks(Integer.valueOf(amountOfTasks));
        personalExam.setAnswers(answers);
        try {
            FieldUtils.writeField(personalExam, "status", examStatus, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        getPersonalExamConnector().save(personalExam);
    }

    @Given("^personal \"(.*)\" exam(?: with examId (.*)|) for student with (.*) id and (.*) status exist and time (.*) and amountOfTasks (.*)$")
    public void personalExamExist(String examName, String examAssigneeId, String studentId, String answerStatus, String time, String amountOfTasks, List<Answer> answers) {
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
            personalExam.start();
            if(StringUtils.isNotEmpty(examAssigneeId)) {
                personalExam.setExamAssigneeId(Long.valueOf(examAssigneeId));
            }
        }
        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);
    }

    @Given("^this teacher review personal exam with id (.*) and exam name (.*) with such grades")
    public void thisTeacherReviewPersonalExamWithIdAbcAndExamNameAndDate(String id, String examName, DataTable grades) {
        String studentUid = getTestExecutionContext().getDetails().getStudentUid();
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentUid);
        personalExam.finish();
        List<Answer> answers = personalExam.getAnswers();
        List<Map<String,String>> gradesMaps = grades.asMaps();
        for(Map<String,String> map : gradesMaps){
            Long taskId = Long.parseLong(map.get("taskId"));
            Grade newTeacherGrade = new Grade();
            newTeacherGrade.setValue(Double.parseDouble(map.get("teacherGrade")));
            answers.stream().filter(answer -> Objects.equals(answer.getTask().getId(), taskId)).toList().get(0).setTeacherGrade(newTeacherGrade);
        }
        personalExam.setAnswers(answers);

        getTestExecutionContext().getDetails().setPersonalExamToReview(personalExam);
    }
}
