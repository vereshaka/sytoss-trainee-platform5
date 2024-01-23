package com.sytoss.lessons.dto.exam.assignees;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sytoss.lessons.dto.Auditable;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "EXAM")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_EXAM", columnNames = {"NAME", "DISCIPLINE_ID"})})
public class ExamDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_id_generator")
    @SequenceGenerator(name = "exam_id_generator", sequenceName = "EXAM_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

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

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "exam")
    private List<ExamAssigneeDTO> examAssignees = new ArrayList<>();

    @Column(name = "MAX_GRADE")
    private Integer maxGrade;

    @ManyToOne
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID")
    private DisciplineDTO discipline;

    @Column(name = "CREATION_DATE")
    private Date creationDate = new Date();
}
