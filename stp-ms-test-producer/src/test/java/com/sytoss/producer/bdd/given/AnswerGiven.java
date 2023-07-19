package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Given;

import java.util.List;

public class AnswerGiven extends TestProducerIntegrationTest {

    @Given("^personal exam with id (.*) and student (.*) and (.*) max grade and sum of coef (.*) exist$")
    public void personalExamWithIdExistsAndAnswersExist(String examId, String studentId, String max_grade, String sumOfCoef, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(examId);
        Student student = new Student();
        student.setUid(studentId);
        personalExam.setStudent(student);
        personalExam.setAnswers(answers);
        personalExam.setMaxGrade(Double.parseDouble(max_grade));
        personalExam.setSumOfCoef(Double.parseDouble(sumOfCoef));
        getPersonalExamConnector().save(personalExam);
        getTestExecutionContext().getDetails().setStudentId(personalExam.getStudent().getUid());
    }
}
