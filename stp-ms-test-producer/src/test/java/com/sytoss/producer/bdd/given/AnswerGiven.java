package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Given;

import java.util.Date;
import java.util.List;

public class AnswerGiven extends TestProducerIntegrationTest {

    @Given("^this student has personal exam with id (.*) with (.*) max grade and sum of coef (.*) exist$")
    public void personalExamWithIdExistsAndAnswersExist(String examId, String max_grade, String sumOfCoef, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();
        Student student = new Student();
        student.setId(getTestExecutionContext().getDetails().getStudentId());
        personalExam.setStudent(student);
        personalExam.setAnswers(answers);
        personalExam.setMaxGrade(Double.parseDouble(max_grade));
        personalExam.setSumOfCoef(Double.parseDouble(sumOfCoef));
        personalExam.setRelevantFrom(new Date());
        personalExam.setRelevantTo(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        personalExam = getPersonalExamConnector().save(personalExam);
        getTestExecutionContext().registerId(examId, personalExam.getId());
        //getTestExecutionContext().getDetails().setStudentUid(personalExam.getStudent().getUid());
    }
}
