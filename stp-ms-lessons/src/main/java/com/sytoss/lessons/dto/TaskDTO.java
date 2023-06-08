package com.sytoss.lessons.dto;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TASK2TOPIC",
            joinColumns = @JoinColumn(name = "TASK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    private List<TopicDTO> topics;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TASK2TASK_CONDITION",
            joinColumns = @JoinColumn(name = "TASK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_CONDITION_ID"))
    private List<TaskConditionDTO> conditions;
}
