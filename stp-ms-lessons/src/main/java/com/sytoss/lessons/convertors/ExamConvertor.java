package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamConvertor {

    @Autowired
    private GroupConvertor groupConvertor;

    @Autowired
    private TopicConvertor topicConvertor;

    public void toDTO(Exam source, ExamDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        GroupDTO groupDTO = new GroupDTO();
        groupConvertor.toDTO(source.getGroup(), groupDTO);

        destination.setGroup(groupDTO);

        List<TopicDTO> topicDTOList = new ArrayList<>();

        source.getTopics().forEach(topic -> {
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            topicDTOList.add(topicDTO);
        });

        destination.setTopics(topicDTOList);
        destination.setNumberOfTasks(source.getNumberOfTasks());
    }

    public void fromDTO(ExamDTO source, Exam destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        Group group = new Group();
        groupConvertor.fromDTO(source.getGroup(), group);

        List<Topic> topicList = new ArrayList<>();

        source.getTopics().forEach(topicDTO -> {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        });

        destination.setGroup(group);
        destination.setTopics(topicList);
        destination.setNumberOfTasks(source.getNumberOfTasks());
    }
}
