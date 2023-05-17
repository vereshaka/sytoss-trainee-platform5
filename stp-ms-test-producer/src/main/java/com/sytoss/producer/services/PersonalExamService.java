package com.sytoss.producer.services;

import com.sytoss.producer.bom.*;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PersonalExamService {

    @Autowired
    private MetadataConnectorImpl metadataConnector;

    @Autowired
    private PersonalExamConnector personalExamConnector;

    public PersonalExam create(ExamConfiguration examConfiguration) {
        PersonalExam personalExam = new PersonalExam();
        int numberOfTasks = examConfiguration.getQuantityOfTask();
        List<Answer> personalTestTask = new ArrayList<>();
        List<Task> allTaskList = new ArrayList<>();
        for (int j = 0; j <= examConfiguration.getTopics().size()-1; j++) {
            List<Task> taskList = metadataConnector.getTasksForTopic(examConfiguration.getTopics().get(j));
            allTaskList.addAll(taskList);
        }
        for (int i = 0; i <= numberOfTasks; i++) {
            Random random = new Random();
            int numTask = random.nextInt(allTaskList.size());
            Task task = allTaskList.get(numTask);
            if (personalTestTask.size() != numberOfTasks) {
                Answer answer = new Answer();
                answer.setStatus(AnswerStatus.NOT_STARTED);
                answer.setTask(task);
                personalTestTask.add(answer);
                allTaskList.remove(task);
            }
        }
        Discipline discipline = metadataConnector.getDiscipline(examConfiguration.getDisciplineId());
        personalExam.setDiscipline(discipline);
        personalExam.setName(examConfiguration.getExamName());
        personalExam.setDate(new Date());
        personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
        personalExam.setAnswers(personalTestTask);
        personalExam.setStudentId(examConfiguration.getStudentId());
        personalExam = personalExamConnector.save(personalExam);
        return personalExam;
    }
}
