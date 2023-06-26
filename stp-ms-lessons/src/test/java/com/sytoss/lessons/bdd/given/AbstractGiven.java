package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;

import java.util.List;

public class AbstractGiven extends CucumberIntegrationTest {

    protected void deleteTopics(List<TopicDTO> topics) {
        for (TopicDTO topicDTO : topics) {
            deleteExams(getExamConnector().findByTopicsId(topicDTO.getId()));
            deleteTasks(getTaskConnector().findByTopicsId(topicDTO.getId()));
        }
        getTopicConnector().deleteAll(topics);
    }

    protected void deleteTasks(List<TaskDTO> tasks) {
        //TODO: yevgenyv: if tasks contains references this operation should be failed
        getTaskConnector().deleteAll(tasks);
    }

    protected void deleteExams(List<ExamDTO> exams) {
        getExamConnector().deleteAll(exams);
    }
}
