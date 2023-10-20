package com.sytoss.domain.bom;

import com.sytoss.domain.bom.personalexam.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamTest {

    @Test
    public void shouldSetSummaryGrade() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setAnswers(List.of(createNewAnswerWithGrade(1), createNewAnswerWithGrade(2), createNewAnswerWithGrade(0.7)));

        personalExam.summary();

        assertEquals(personalExam.getSummaryGrade(), 3.7);
    }

    private Answer createNewAnswerWithGrade(double value) {
        Grade grade = new Grade();
        grade.setValue(value);

        Answer answer = new Answer();
        answer.setGrade(grade);
        answer.setTeacherGrade(grade);
        answer.setStatus(AnswerStatus.GRADED);

        return answer;
    }
}
