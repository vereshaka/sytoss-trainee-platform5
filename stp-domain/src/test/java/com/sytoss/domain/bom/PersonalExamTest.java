package com.sytoss.domain.bom;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamTest {

    @Test
    public void shouldSetSummaryGrade() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setAnswers(List.of(createNewAnswerWithGrade(1f), createNewAnswerWithGrade(2f), createNewAnswerWithGrade(0.7f)));

        personalExam.summary();

        assertEquals(personalExam.getSummaryGrade(), 3.7f);
    }

    private Answer createNewAnswerWithGrade(float value) {
        Score score = new Score();
        score.setValue(value);

        Answer answer = new Answer();
        answer.setScore(score);
        answer.setStatus(AnswerStatus.GRADED);

        return answer;
    }
}
