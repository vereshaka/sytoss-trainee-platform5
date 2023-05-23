package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;

import java.util.List;
import java.util.Objects;

public class MetadataConnectorImpl implements MetadataConnector {

    @Override
    public Discipline getDiscipline(String id) {
        Discipline discipline = new Discipline();
        if (Objects.equals(id, "1L")) {
            discipline.setId(id);
            discipline.setName("SQL");
        } else if (Objects.equals(id, "2L")) {
            discipline.setId(id);
            discipline.setName("ORACLE");
        } else {
            discipline.setId(id);
            discipline.setName("Java");
        }
        return discipline;
    }

    @Override
    public Topic getTopic(String id) {
        Topic topic = new Topic();
        if (Objects.equals(id, "1L")) {
            topic.setId(id);
            topic.setName("JOIN");
            topic.setDiscipline(getDiscipline("1L"));
        } else if (Objects.equals(id, "2L")) {
            topic.setId(id);
            topic.setName("SELECT");
            topic.setDiscipline(getDiscipline("2L"));
        } else {
            topic.setId(id);
            topic.setName("Sorting results");
            topic.setDiscipline(getDiscipline("3L"));
        }
        return topic;
    }

    @Override
    public List<Task> getTasksForTopic(String id) {
        if (Objects.equals(id, "1L")) {
            return List.of(createTask("Inner Join", "Yes", List.of(getTopic("1L")), getTaskDomain(1L)),
                    createTask("Left Join", "Yes", List.of(getTopic("1L")), getTaskDomain(1L)));
        } else if (Objects.equals(id, "2L")) {
            return List.of(createTask("Left Join?", "Yes", List.of(getTopic("2L")), getTaskDomain(1L)),
                    createTask("Is SQL cool?", "Yes", List.of(getTopic("2L")), getTaskDomain(1L)));
        } else {
            return List.of(createTask("Inner Join?", "Yes", List.of(getTopic("3L")), getTaskDomain(1L)),
                    createTask("Left Join?", "Yes", List.of(getTopic("3L")), getTaskDomain(1L)),
                    createTask("SELECT?", "Yes", List.of(getTopic("3L")), getTaskDomain(1L)));
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

    public Task createTask(String question, String etalonAnswer, List<Topic> topics, TaskDomain taskDomain) {
        Task task = new Task();
        task.setTaskDomain(taskDomain);
        task.setQuestion(question);
        task.setTopics(topics);
        task.setEtalonAnswer(etalonAnswer);
        return task;
    }
}
