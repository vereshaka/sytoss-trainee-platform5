package com.sytoss.producer.bom;

import com.sytoss.domain.bom.exceptions.business.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.exceptions.business.PersonalExamIsFinishedException;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonalExamTest {

    @Test
    public void shouldStartPersonalExam() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.start();
        assertEquals(PersonalExamStatus.IN_PROGRESS, personalExam.getStatus());
    }

    @Test
    public void shouldNotStartPersonalExamWhenItStarted() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.start();
        assertThrows(PersonalExamAlreadyStartedException.class, personalExam::start);
    }

    @Test
    public void shouldNotStartPersonalExamWhenItFinished() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.finish();
        assertThrows(PersonalExamIsFinishedException.class, personalExam::start);
    }
}
