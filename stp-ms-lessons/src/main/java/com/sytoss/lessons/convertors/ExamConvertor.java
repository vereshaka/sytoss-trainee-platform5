package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamConvertor {

    private final TopicConvertor topicConvertor;
    private final TaskConvertor taskConvertor;
    private final ExamAssigneeConvertor examAssigneeConvertor;

    public void toDTO(Exam source, ExamDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());

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
        destination.setMaxGrade(source.getMaxGrade());

        if (!source.getExamAssignees().isEmpty()) {
            for (ExamAssignee examAssignee : source.getExamAssignees()) {
                ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
                examAssigneeConvertor.toDTO(examAssignee, examAssigneeDTO);
                destination.getExamAssignees().add(examAssigneeDTO);
            }
        }
    }

    public void fromDTO(ExamDTO source, Exam destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        if (source.getDiscipline() != null) {
            destination.setDiscipline(new Discipline());
            destination.getDiscipline().setId(source.getDiscipline().getId());
        }

        List<Topic> topicList = new ArrayList<>();

        source.getTopics().forEach(topicDTO -> {
            Topic topic = new Topic();
            topicConvertor.fromDTO(topicDTO, topic);
            topicList.add(topic);
        });
        List<Task> tasksList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(source.getTasks())) {
            source.getTasks().forEach(taskDTO -> {
                Task task = new Task();
                taskConvertor.fromDTO(taskDTO, task);
                tasksList.add(task);
            });
        }

        destination.setTopics(topicList);
        destination.setTasks(tasksList);
        destination.setNumberOfTasks(source.getNumberOfTasks());
        Teacher teacher = new Teacher();
        teacher.setId(source.getTeacherId());
        destination.setTeacher(teacher);
        destination.setMaxGrade(source.getMaxGrade());

        for (ExamAssigneeDTO examAssigneeDTO : source.getExamAssignees()) {
            ExamAssignee examAssignee = new ExamAssignee();
            for (ExamAssigneeToDTO item : examAssigneeDTO.getExamAssigneeToDTOList()) {
                if (item instanceof ExamToGroupAssigneeDTO) {
                    examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
                    for (ExamAssigneeToDTO examToGroupAssigneeDTO : examAssigneeDTO.getExamAssigneeToDTOList()) {
                        Group group = new Group();
                        group.setId(((ExamToGroupAssigneeDTO) examToGroupAssigneeDTO).getGroupId());
                        examAssignee.getGroups().add(group);
                    }
                } else {
                    examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
                    for (ExamAssigneeToDTO examToStudentAssigneeDTO : examAssigneeDTO.getExamAssigneeToDTOList()) {
                        Student student = new Student();
                        student.setId(((ExamToStudentAssigneeDTO) examToStudentAssigneeDTO).getStudentId());
                        examAssignee.getStudents().add(student);
                    }
                }
            }
            destination.getExamAssignees().add(examAssignee);
        }
    }
}
