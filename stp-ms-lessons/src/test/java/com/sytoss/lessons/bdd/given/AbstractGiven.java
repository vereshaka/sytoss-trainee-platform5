package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;

import java.util.List;

public class AbstractGiven extends LessonsIntegrationTest {

    protected void deleteTopics(List<TopicDTO> topics){
        //TODO: yevgenyv: if topics contains references this operation should be failed
        getTopicConnector().deleteAll(topics);
    }

    protected void deleteTasks(List<TaskDTO> tasks){
        //TODO: yevgenyv: if tasks contains references this operation should be failed
        getTaskConnector().deleteAll(tasks);
    }
}
