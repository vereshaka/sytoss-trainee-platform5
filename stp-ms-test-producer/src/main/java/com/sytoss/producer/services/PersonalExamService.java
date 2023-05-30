package com.sytoss.producer.services;

import com.sytoss.domain.bom.exceptions.businessException.PersonalExamHasNoAnswerException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalExamService {

    private final MetadataConnectorImpl metadataConnector = new MetadataConnectorImpl();

    private final PersonalExamConnector personalExamConnector;

    public PersonalExam create(ExamConfiguration examConfiguration) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setDiscipline(getDiscipline(examConfiguration.getDisciplineId()));
        personalExam.setName(examConfiguration.getExamName());
        personalExam.setDate(new Date());
        personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
        personalExam.setAnswers(generateAnswers(examConfiguration.getQuantityOfTask(), examConfiguration));
        personalExam.setStudentId(examConfiguration.getStudentId());
        personalExam = personalExamConnector.save(personalExam);
        return personalExam;
    }

    private List<Answer> generateAnswers(int numberOfTasks, ExamConfiguration examConfiguration) {
        List<Task> tasks = examConfiguration.getTopics().stream()
                .flatMap(topic -> metadataConnector.getTasksForTopic(topic).stream())
                .collect(Collectors.toList());
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i <= numberOfTasks - 1; i++) {
            Random random = new Random();
            int numTask = random.nextInt(tasks.size());
            Task task = tasks.get(numTask);
            Answer answer = new Answer();
            answer.setStatus(AnswerStatus.NOT_STARTED);
            answer.setTask(task);
            answers.add(answer);
            tasks.remove(task);
        }
        return answers;
    }

    private Discipline getDiscipline(Long disciplineId) {
        return metadataConnector.getDiscipline(disciplineId);
    }

    public PersonalExam summary(String id) {
        PersonalExam personalExam = personalExamConnector.getById(id);
        personalExam.summary();
        return personalExam;
    }

    public Task start(String personalExamId) {
        PersonalExam personalExam = getById(personalExamId);
        if (personalExam.getAnswers().isEmpty()) {
            throw new PersonalExamHasNoAnswerException();
        }
        personalExam.start();
        personalExam.getAnswers().get(0).inProgress();
        personalExamConnector.save(personalExam);
        return personalExam.getAnswers().get(0).getTask();
    }

    public PersonalExam getById(String personalExamId) {
        return personalExamConnector.getById(personalExamId);
    }
}
