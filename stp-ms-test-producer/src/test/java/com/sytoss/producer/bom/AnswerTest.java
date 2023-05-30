package com.sytoss.producer.bom;

import com.sytoss.domain.bom.exceptions.business.AnswerInProgressException;
import com.sytoss.domain.bom.exceptions.business.AnswerIsAnsweredException;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnswerTest {

    @Test
    public void shouldChangeStatusOnInProgress() {
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.NOT_STARTED);
        answer.inProgress();
        assertEquals(AnswerStatus.IN_PROGRESS, answer.getStatus());
    }

    @Test
    public void shouldNotChangeStatusOnInProgressWhenAnswerIsInProgress() {
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.IN_PROGRESS);
        assertThrows(AnswerInProgressException.class, () -> answer.inProgress());
    }

    @Test
    public void shouldNotChangeStatusOnInProgressWhenAnswerIsAnswered() {
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.ANSWERED);
        assertThrows(AnswerIsAnsweredException.class, () -> answer.inProgress());
    }

    @Test
    public void shouldNotChangeStatusOnInProgressWhenAnswerIsGraded() {
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.GRADED);
        assertThrows(AnswerIsAnsweredException.class, () -> answer.inProgress());
    }
}