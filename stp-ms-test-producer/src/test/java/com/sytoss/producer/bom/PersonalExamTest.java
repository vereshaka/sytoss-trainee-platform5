package com.sytoss.producer.bom;

import com.sytoss.producer.exceptions.businessException.PersonalExamAlreadyStartedException;
import com.sytoss.producer.exceptions.businessException.PersonalExamIsFinishedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonalExamTest {

    @Test
    public void shouldStartPersonalExam() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
        personalExam.start();
        assertEquals(PersonalExamStatus.IN_PROGRESS, personalExam.getStatus());
    }

    @Test
    public void shouldNotStartPersonalExamWhenItStarted() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setStatus(PersonalExamStatus.IN_PROGRESS);
        assertThrows(PersonalExamAlreadyStartedException.class, () -> personalExam.start());
    }

    @Test
    public void shouldNotStartPersonalExamWhenItFinished() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setStatus(PersonalExamStatus.FINISHED);
        assertThrows(PersonalExamIsFinishedException.class, () -> personalExam.start());
    }
}
