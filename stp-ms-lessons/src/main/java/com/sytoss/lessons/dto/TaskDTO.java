package com.sytoss.lessons.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "TASK")
public class TaskDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "TASK_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ETALON_ANSWER")
    private String etalonAnswer;

    @OneToOne
    @JoinColumn(name = "TASK_DOMAIN_ID", referencedColumnName = "ID")
    private TaskDomainDTO taskDomain;

    @OneToMany(mappedBy = "task")
//    @JoinColumn(name = "TOPIC_ID", referencedColumnName = "ID")
    private List<TopicDTO> topics;
}
