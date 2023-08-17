package com.sytoss.lessons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity(name = "EXAM")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ExamDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_id_generator")
    @SequenceGenerator(name = "exam_id_generator", sequenceName = "EXAM_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "RELEVANT_FROM")
    private Date relevantFrom;

    @Column(name = "RELEVANT_TO")
    private Date relevantTo;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "NUMBER_OF_TASKS")
    private Integer numberOfTasks;

    @Column(name = "TEACHER_ID")
    private Long teacherId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "EXAM2TOPIC",
            joinColumns = @JoinColumn(name = "EXAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    private Collection<TopicDTO> topics;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "EXAM2TASK",
            joinColumns = @JoinColumn(name = "EXAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"))
    private Collection<TaskDTO> tasks;

    @Column(name = "MAX_GRADE")
    private Integer maxGrade;
}