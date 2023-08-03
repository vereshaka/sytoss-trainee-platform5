package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.apache.commons.lang3.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamConvertor {

    private final TopicConvertor topicConvertor;
    private final TaskConvertor taskConvertor;

    public void toDTO(Exam source, ExamDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        if (ObjectUtils.isNotEmpty(source.getGroup())) {
            destination.setGroupId(source.getGroup().getId());
        }

        List<TopicDTO> topicDTOList = new ArrayList<>();

        source.getTopics().forEach(topic -> {
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            topicDTOList.add(topicDTO);
        });

        destination.setTopics(topicDTOList);

        List<TaskDTO> taskDTOList = new ArrayList<>();

        source.getTasks().forEach(task -> {
            TaskDTO taskDTO = new TaskDTO();
            taskConvertor.toDTO(task, taskDTO);
            taskDTOList.add(taskDTO);
        });

        destination.setTasks(taskDTOList);
        destination.setNumberOfTasks(source.getNumberOfTasks());
        destination.setTeacherId(source.getTeacher().getId());
    }

    public void fromDTO(ExamDTO source, Exam destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        if (ObjectUtils.isNotEmpty(source.getGroupId())) {
            Group group = new Group();
            group.setId(source.getGroupId());
            destination.setGroup(group);
        }

        List<Topic> topicList = new ArrayList<>();

        source.getTopics().forEach(topicDTO -> {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        });

        destination.setTopics(topicList);
        destination.setNumberOfTasks(source.getNumberOfTasks());
        Teacher teacher = new Teacher();
        teacher.setId(source.getTeacherId());
        destination.setTeacher(teacher);
    }
}
