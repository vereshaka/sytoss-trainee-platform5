package com.sytoss.producer.convertor;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.interfaces.AnswerGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PersonalExamConvertor {

    private final AnswerGenerator answerGenerator;

    public void fromExamConfiguration(ExamConfiguration source, PersonalExam destination){
        Date relevantFrom = source.getExamAssignee().getRelevantFrom();
        Date relevantTo = source.getExamAssignee().getRelevantTo();
        destination.setName(source.getExam().getName());
        destination.setExamAssigneeId(source.getExamAssignee().getId());
        destination.setAssignedDate(new Date());
        destination.setRelevantFrom(relevantFrom);
        destination.setRelevantTo(relevantTo);
        destination.setDiscipline(source.getExam().getDiscipline());
        destination.setTeacher(source.getExam().getTeacher());
        destination.setTime((int) TimeUnit.MILLISECONDS.toSeconds(relevantTo.getTime() - relevantFrom.getTime()));
        destination.setAmountOfTasks(source.getExam().getNumberOfTasks());
        destination.setMaxGrade(source.getExam().getMaxGrade());
        List<Answer> answers = answerGenerator.generateAnswers(source.getExam().getNumberOfTasks(), source.getExam().getTasks());
        destination.setAnswers(answers);
        double sumOfCoef = 0;
        for (Answer answer : answers) {
            sumOfCoef += answer.getTask().getCoef();
        }
        destination.setStudent(source.getStudent());
        destination.setSumOfCoef(sumOfCoef);
    }
}
