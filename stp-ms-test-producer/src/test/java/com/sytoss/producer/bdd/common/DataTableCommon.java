package com.sytoss.producer.bdd.common;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.DataTableType;

import java.util.List;
import java.util.Map;

public class DataTableCommon {

    @DataTableType
    public Answer mapAnswer(Map<String, String> params) {
        Answer answer = new Answer();

        if (params.containsKey("answerId")) {
            answer.setId(Long.valueOf(params.get("answerId")));
        }
        if (params.containsKey("answer")) {
            answer.setValue(params.get("answer"));
        }
        if (params.containsKey("status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("status")));
        }
        if (params.containsKey("task status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("task status")));
        }

        Task task = new Task();
        if (params.containsKey("taskId")) {
            task.setId(Long.valueOf(params.get("taskId")));
        }
        if (params.containsKey("question")) {
            task.setQuestion(params.get("question"));
        }
        if (params.containsKey("etalon")) {
            task.setEtalonAnswer(params.get("etalon"));
        }
        if (params.containsKey("coef")) {
            task.setCoef(Double.parseDouble(params.get("coef")));
        }
        if (params.containsKey("task")) {
            task.setQuestion(params.get("task"));
            answer.setTask(task);
        }
        TaskDomain taskDomain = new TaskDomain();
        if (params.containsKey("taskDomainId")) {
            taskDomain.setId(Long.valueOf(params.get("taskDomainId")));
            task.setTaskDomain(taskDomain);
        }

        Grade grade = new Grade();
        if (params.containsKey("grade") && params.get("grade") != null) {
            grade.setValue(Float.parseFloat(params.get("grade")));
            grade.setComment(params.get("comment"));
            answer.setGrade(grade);
        }

        Topic topic = new Topic();
        if (params.containsKey("listOfSubjects")) {
            topic.setName(params.get("listOfSubjects"));
            task.setTopics(List.of(topic));
        }

        if (params.containsKey("task status")) {
            answer.setStatus(AnswerStatus.valueOf(params.get("task status")));
        }

        if (params.containsKey("script")) {
            taskDomain.setScript(params.get("script"));
            task.setTaskDomain(taskDomain);
            answer.setTask(task);
        }
        answer.setTask(task);

        return answer;
    }
}
