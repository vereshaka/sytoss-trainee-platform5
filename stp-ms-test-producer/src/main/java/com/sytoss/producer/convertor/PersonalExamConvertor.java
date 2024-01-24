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

    public void fromExamConfiguration(ExamConfiguration examConfiguration, PersonalExam personalExam) {
        Date relevantFrom = examConfiguration.getExamAssignee().getRelevantFrom();
        Date relevantTo = examConfiguration.getExamAssignee().getRelevantTo();
        personalExam.setName(examConfiguration.getExam().getName());
        personalExam.setExamAssigneeId(examConfiguration.getExamAssignee().getId());
        personalExam.setAssignedDate(new Date());
        personalExam.setRelevantFrom(relevantFrom);
        personalExam.setRelevantTo(relevantTo);
        personalExam.setDiscipline(examConfiguration.getExam().getDiscipline());
        personalExam.setTeacher(examConfiguration.getExam().getTeacher());
        personalExam.setTime((int) TimeUnit.MILLISECONDS.toSeconds(relevantTo.getTime() - relevantFrom.getTime()));
        personalExam.setAmountOfTasks(examConfiguration.getExam().getNumberOfTasks());
        personalExam.setMaxGrade(examConfiguration.getExam().getMaxGrade());
        List<Answer> answers = answerGenerator.generateAnswers(examConfiguration.getExam().getNumberOfTasks(), examConfiguration.getExam().getTasks());
        personalExam.setAnswers(answers);
        double sumOfCoef = 0;
        for (Answer answer : answers) {
            sumOfCoef += answer.getTask().getCoef();
        }
        personalExam.setStudent(examConfiguration.getStudent());
        personalExam.setSumOfCoef(sumOfCoef);
    }
}
