package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import com.sytoss.stp.test.FileUtils;
import io.cucumber.java.en.Given;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AnswerGiven extends TestProducerIntegrationTest {

    @Given("^this student has personal exam with id (.*) with (.*) max grade and sum of coef (.*) exist$")
    public void personalExamWithIdExistsAndAnswersExist(String examId, String max_grade, String sumOfCoef, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId(examId);
        Student student = new Student();
        student.setId(getTestExecutionContext().getDetails().getStudentId());
        personalExam.setStudent(student);

        Task task = new Task();
        task.setId(13L);
        task.setCoef(1.0);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setDatabaseScript(FileUtils.readFromFile("scripts/database.puml"));
        taskDomain.setDataScript(FileUtils.readFromFile("scripts/data.puml"));
        task.setTaskDomain(taskDomain);

        for(Answer answer : answers){
            answer.setTask(task);
        }

        personalExam.setAnswers(answers);

        when(getMetadataConnector().getTaskById(anyLong())).thenReturn(task);
        when(getMetadataConnector().getTaskDomain(anyLong())).thenReturn(taskDomain);

        personalExam.setId("1");
        personalExam.setStudent(student);
        personalExam.setAnswers(answers);
        personalExam.setMaxGrade(Double.parseDouble(max_grade));
        personalExam.setSumOfCoef(Double.parseDouble(sumOfCoef));
        personalExam.setRelevantFrom(new Date());
        personalExam.setRelevantTo(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        getPersonalExamConnector().save(personalExam);
        getTestExecutionContext().registerId(examId, personalExam.getId());
        //getTestExecutionContext().getDetails().setStudentUid(personalExam.getStudent().getUid());
    }
}
