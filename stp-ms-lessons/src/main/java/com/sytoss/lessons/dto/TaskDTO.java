package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "TASK")
public class TaskDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "TASK_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ETALON_ANSWER")
    private String etalonAnswer;

    @Column(name = "COEFFICIENT")
    private Double coef;

    @Column(name = "IS_SORTING_RELEVANT")
    private Boolean isSortingRelevant;

    @Column(name = "DATABASE_TYPE")
    private String databaseType;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TASK_DOMAIN_ID", referencedColumnName = "ID")
    private TaskDomainDTO taskDomain;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TASK2TOPIC",
            joinColumns = @JoinColumn(name = "TASK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    private List<TopicDTO> topics;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TASK_ID", referencedColumnName = "ID")
    private List<TaskConditionDTO> conditions = new ArrayList<>();

    @Column(name = "CODE")
    private String code;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CHECK_ANSWER")
    private String checkAnswer;

    @Column(name = "MODE")
    private String mode;
}