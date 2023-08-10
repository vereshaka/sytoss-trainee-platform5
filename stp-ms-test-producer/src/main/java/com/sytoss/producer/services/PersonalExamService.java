package com.sytoss.producer.services;

import com.sytoss.common.AbstractStpService;
import com.sytoss.domain.bom.exceptions.business.PersonalExamHasNoAnswerException;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.exceptions.business.notfound.PersonalExamNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.ImageConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonalExamService extends AbstractStpService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

   private final ImageConnector imageConnector;

    public PersonalExam create(ExamConfiguration examConfiguration) {
        PersonalExam personalExam = new PersonalExam();
        //TODO: yevgeyv: fix it personalExam.setDiscipline(getDiscipline(examConfiguration.getExam().get);
        personalExam.setName(examConfiguration.getExam().getName());
        personalExam.setExamId(examConfiguration.getExam().getId());
        personalExam.setAssignedDate(new Date());
        personalExam.setTime(examConfiguration.getExam().getDuration() == null ? 5 : examConfiguration.getExam().getDuration());
        personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
        List<Answer> answers = generateAnswers(examConfiguration.getExam().getNumberOfTasks(), examConfiguration.getExam().getTasks());
        personalExam.setAnswers(answers);
        for (Answer answer : answers) {
            answer.getTask().setImageId(imageConnector.convertImage(answer.getTask().getQuestion()));
        }
        personalExam.setStudent(examConfiguration.getStudent());
        personalExam = personalExamConnector.save(personalExam);
        return personalExam;
    }

    private List<Answer> generateAnswers(int numberOfTasks, List<Task> tasks) {
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

    public boolean taskDomainIsUsed(Long taskDomainId) {
        int count = personalExamConnector.countByAnswersTaskTaskDomainIdAndStatusNotLike(taskDomainId, PersonalExamStatus.FINISHED);
        return count > 0;
    }

    private Discipline getDiscipline(Long disciplineId) {
        return metadataConnector.getDiscipline(disciplineId);
    }

    public PersonalExam summary(String id) {
        PersonalExam personalExam = personalExamConnector.getById(id);
        personalExam.summary();
        return personalExam;
    }

    public Question start(String personalExamId) {
        String studentId = getMyId();
        PersonalExam personalExam = getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getUid(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(personalExamId, studentId);
        }
        if (personalExam.getAnswers().isEmpty()) {
            throw new PersonalExamHasNoAnswerException();
        }
        personalExam.start();
        personalExam.getAnswers().get(0).inProgress();
        personalExam = personalExamConnector.save(personalExam);
        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime(personalExam.getTime());
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        taskModel.setQuestion(personalExam.getAnswers().get(0).getTask().getQuestion());
        taskModel.setSchema(personalExam.getAnswers().get(0).getTask().getTaskDomain().getDatabaseScript());
        taskModel.setQuestionNumber(1);
        firstTask.setTask(taskModel);
        return firstTask;
    }

    public PersonalExam getById(String personalExamId) {
        return personalExamConnector.getById(personalExamId);
    }

    public List<PersonalExam> getAllByExamId(Long examId) {
        return personalExamConnector.getAllByExamId(examId);
    }

    public List<PersonalExam> getByUserId(Long userId) {
        return personalExamConnector.getAllByStudent_Id(userId);
    }

    public PersonalExam review(PersonalExam personalExamToChange) {
        PersonalExam personalExam = getById(personalExamToChange.getId());

        if (ObjectUtils.isEmpty(personalExam)) {
            throw new PersonalExamNotFoundException(personalExamToChange.getId());
        }

        personalExamToChange.getAnswers().forEach(
                answerToChange -> {
                    Answer answer = personalExam.getAnswerById(answerToChange.getId());
                    answer.getTeacherGrade().setValue(answerToChange.getTeacherGrade().getValue());
                }
        );

        personalExam.setStatus(PersonalExamStatus.REVIEWED);
        return personalExamConnector.save(personalExam);
    }
}
