package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.Discipline;
import com.sytoss.domain.bom.Task;
import com.sytoss.domain.bom.TaskDomain;
import com.sytoss.domain.bom.Topic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MetadataConnectorImpl implements MetadataConnector {

    @Override
    public Discipline getDiscipline(Long id) {
        Discipline discipline = new Discipline();
        if (id == 1L) {
            discipline.setId(id);
            discipline.setName("SQL");
        } else if (id == 2L) {
            discipline.setId(id);
            discipline.setName("ORACLE");
        } else {
            discipline.setId(id);
            discipline.setName("Java");
        }
        return discipline;
    }

    @Override
    public Topic getTopic(Long id) {
        Topic topic = new Topic();
        if (id == 1L) {
            topic.setId(id);
            topic.setName("JOIN");
            topic.setDiscipline(getDiscipline(1L));
        } else if (id == 2L) {
            topic.setId(id);
            topic.setName("SELECT");
            topic.setDiscipline(getDiscipline(2L));
        } else {
            topic.setId(id);
            topic.setName("Sorting results");
            topic.setDiscipline(getDiscipline(3L));
        }
        return topic;
    }

    @Override
    public List<Task> getTasksForTopic(Long id) {
        if (id == 1L) {
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Inner Join");
            task.setTopics(List.of(getTopic(1L)));
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Left Join");
            taskSecond.setTopics(List.of(getTopic(1L)));
            taskSecond.setEtalonAnswer("Yes");
            return List.of(task, taskSecond);
        } else if (id == 2L) {
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Left Join?");
            task.setTopics(List.of(getTopic(2L)));
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Is SQL cool?");
            taskSecond.setTopics(List.of(getTopic(2L)));
            taskSecond.setEtalonAnswer("Yes");
            return List.of(task, taskSecond);
        } else {
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Inner Join?");
            task.setTopics(List.of(getTopic(3L)));
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Left Join?");
            taskSecond.setTopics(List.of(getTopic(3L)));
            taskSecond.setEtalonAnswer("Yes");
            Task taskThird = new Task();
            taskThird.setTaskDomain(getTaskDomain(1L));
            taskThird.setQuestion("SELECT?");
            taskThird.setTopics(List.of(getTopic(3L)));
            taskThird.setEtalonAnswer("Yes");
            return List.of(task, taskSecond, taskThird);

        }
    }

    @Override
    public TaskDomain getTaskDomain(Long id) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(id);
        if (id == 1L) {
            taskDomain.setName("TaskDomainOne");
            taskDomain.setScript("ScriptOne");
        } else if (id == 2L) {
            taskDomain.setName("TaskDomainTwo");
            taskDomain.setScript("ScriptTwo");
        } else {
            taskDomain.setName("TaskDomainThird");
            taskDomain.setScript("ScriptThird");
        }
        return taskDomain;
    }
}
