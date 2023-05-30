package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
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
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        GroupDTO groupDTO = new GroupDTO();
        groupConvertor.toDTO(source.getGroup(), groupDTO);

        destination.setGroup(groupDTO);
        destination.setTopics(listTopicToDTO(source.getTopics()));
        destination.setNumberOfTasks(source.getNumberOfTasks());
    }

    private List<TopicDTO> listTopicToDTO(List<Topic> topics) {
        List<TopicDTO> result = new ArrayList<>();

        topics.forEach(topic -> {
            TopicDTO topicDTO = new TopicDTO();
            topicConvertor.toDTO(topic, topicDTO);
            result.add(topicDTO);
        });

        return result;
    }
}
