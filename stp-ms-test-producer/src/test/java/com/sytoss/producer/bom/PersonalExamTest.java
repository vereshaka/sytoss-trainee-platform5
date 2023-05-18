package com.sytoss.producer.bom;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamTest {

    @Test
    public void shouldSetSummaryGrade() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setAnswers(List.of(createNewAnswerWithGrade(1f), createNewAnswerWithGrade(2f), createNewAnswerWithGrade(0.7f)));

        personalExam.summary();

        assertEquals(personalExam.getSummaryGrade(), 3.7);
    }

    private Answer createNewAnswerWithGrade(float value) {
        Grade grade = new Grade();
        grade.setValue(value);

        Answer answer = new Answer();
        answer.setGrade(grade);

        return answer;
    }
}
