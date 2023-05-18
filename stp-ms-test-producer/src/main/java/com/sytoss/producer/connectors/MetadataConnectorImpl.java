package com.sytoss.producer.connectors;

import com.sytoss.producer.bom.Discipline;
import com.sytoss.producer.bom.Task;
import com.sytoss.producer.bom.TaskDomain;
import com.sytoss.producer.bom.Topic;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MetadataConnectorImpl implements MetadataConnector {

    @Override
    public Discipline getDiscipline(Long id) {
        Discipline discipline = new Discipline();
        if(id == 1L){
            discipline.setId(id);
            discipline.setName("SQL");
        }else if(id == 2L){
            discipline.setId(id);
            discipline.setName("ORACLE");
        }else {
            discipline.setId(id);
            discipline.setName("Java");
        }
        return discipline;
    }

    @Override
    public Topic getTopic(Long id) {
        Topic topic = new Topic();
        if(id == 1L){
            topic.setId(id);
            topic.setName("JOIN");
            topic.setDiscipline(getDiscipline(1L));
        }else if(id == 2L){
            topic.setId(id);
            topic.setName("SELECT");
            topic.setDiscipline(getDiscipline(2L));
        }else {
            topic.setId(id);
            topic.setName("Sorting results");
            topic.setDiscipline(getDiscipline(3L));
        }
        return topic;
    }

    @Override
    public List<Task> getTasksForTopic(Long id) {
        List<Task> tasks = new ArrayList<>();
        if(id == 1L){
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Inner Join");
            List<Topic> topics = new ArrayList<>();
            topics.add(getTopic(1L));
            task.setTopics(topics);
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Inner Join");
            List<Topic> topicsSecond = new ArrayList<>();
            topicsSecond.add(getTopic(1L));
            taskSecond.setTopics(topicsSecond);
            taskSecond.setEtalonAnswer("Yes");
            tasks.add(task);
            tasks.add(taskSecond);
        } else if(id == 2L){
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Left Join?");
            List<Topic> topics = new ArrayList<>();
            topics.add(getTopic(1L));
            task.setTopics(topics);
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Is SQL cool?");
            List<Topic> topicsSecond = new ArrayList<>();
            topicsSecond.add(getTopic(1L));
            taskSecond.setTopics(topicsSecond);
            taskSecond.setEtalonAnswer("Yes");
            tasks.add(task);
            tasks.add(taskSecond);
        }else {
            Task task = new Task();
            task.setTaskDomain(getTaskDomain(1L));
            task.setQuestion("Inner Join?");
            List<Topic> topics = new ArrayList<>();
            topics.add(getTopic(1L));
            task.setTopics(topics);
            task.setEtalonAnswer("Yes");
            Task taskSecond = new Task();
            taskSecond.setTaskDomain(getTaskDomain(1L));
            taskSecond.setQuestion("Left Join?");
            List<Topic> topicsSecond = new ArrayList<>();
            topicsSecond.add(getTopic(1L));
            taskSecond.setTopics(topicsSecond);
            taskSecond.setEtalonAnswer("Yes");
            Task taskThird = new Task();
            taskThird.setTaskDomain(getTaskDomain(1L));
            taskThird.setQuestion("SELECT?");
            List<Topic> topicsThird = new ArrayList<>();
            topicsSecond.add(getTopic(1L));
            taskThird.setTopics(topicsThird);
            taskThird.setEtalonAnswer("Yes");
            tasks.add(task);
            tasks.add(taskThird);
            tasks.add(taskSecond);
        }
        return tasks;
    }

    @Override
    public TaskDomain getTaskDomain(Long id) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(id);
        if(id == 1L){
            taskDomain.setName("TaskDomainOne");
            taskDomain.setScript("ScriptOne");
        } else if (id == 2L) {
            taskDomain.setName("TaskDomainTwo");
            taskDomain.setScript("ScriptTwo");
        }else {
            taskDomain.setName("TaskDomainThird");
            taskDomain.setScript("ScriptThird");
        }
        return taskDomain;
    }
}
