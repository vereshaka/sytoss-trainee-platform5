package com.sytoss.producer.services;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnswerService extends AbstractService {

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskService checkTaskService;

    public Question answer(String personalExamId, String taskAnswer, Date answerUIDate, Long timeSpent) {
        Long studentId = getCurrentUser().getId();
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getId(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(studentId, personalExamId);
        }
        Answer answer = personalExam.getCurrentAnswer();
        if (answer == null) {
            return null;
        }
        answer.setAnswerDate(new Date());
        answer.setAnswerUIDate(answerUIDate);
        answer.setTimeSpent(timeSpent);
        answer.answer(taskAnswer);
        checkTaskService.checkAnswer(answer, personalExam);
        answer = personalExam.getNextAnswer();
        personalExamConnector.save(personalExam);
        if (answer == null) {
            return null;
        }
        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime((int) TimeUnit.MILLISECONDS.toSeconds(personalExam.getRelevantTo().getTime() - new Date().getTime()));
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        Long processedQuestionsNum = personalExam.getAnswers().stream()
                .filter(item -> item.getStatus().equals(AnswerStatus.ANSWERED)
                        || item.getStatus().equals(AnswerStatus.GRADED))
                .count();
        taskModel.setQuestionNumber((int) (processedQuestionsNum + 1L));
        taskModel.setNeedCheckQuery(answer.getTask().getCheckAnswer() != null);
        firstTask.setTask(taskModel);

        return firstTask;
    }


    public String getDbImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DB);
    }

    public String getDataImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DATA);
    }


    private String getImage(String personalExamId, ConvertToPumlParameters type) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();
        if (type == ConvertToPumlParameters.DB) {
            return answer.getTask().getTaskDomain().getDbImageName();
        } else {
            return answer.getTask().getTaskDomain().getDataImageName();
        }
    }
}