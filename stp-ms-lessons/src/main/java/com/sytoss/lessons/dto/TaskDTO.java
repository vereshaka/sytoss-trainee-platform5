package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Entity(name = "TASK")
public class TaskDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "TASK_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER")
    private String etalonAnswer;

    @OneToOne
    @JoinColumn(name = "TASK_DOMAIN_ID", referencedColumnName = "ID")
    private TaskDomainDTO taskDomainDTO;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TASK2TOPIC",
            joinColumns = @JoinColumn(name = "TASK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    private Collection<TopicDTO> topics;
}