package com.sytoss.lessons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "GROUPS_ID", referencedColumnName = "ID")
    private GroupDTO group;

    @Column(name = "NUMBER_OF_TASKS")
    private Integer numberOfTasks;

    @ManyToMany
    @JoinTable(
            name = "EXAM2TOPIC",
            joinColumns = @JoinColumn(name = "EXAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    private Collection<TopicDTO> topics;
}

