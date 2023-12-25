package com.sytoss.lessons.dto.exam.assignees;

import com.sytoss.lessons.dto.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "EXAM_ASSIGNEE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ExamAssigneeDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_assignee_id_generator")
    @SequenceGenerator(name = "exam_assignee_id_generator", sequenceName = "EXAM_ASSIGNEE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "RELEVANT_FROM")
    private Date relevantFrom;

    @Column(name = "RELEVANT_TO")
    private Date relevantTo;

    @Column(name = "DURATION")
    private Integer duration;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "parent")
    private List<ExamAssigneeToDTO> examAssigneeToDTOList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "EXAM_ID",referencedColumnName = "ID", updatable = false, nullable = false)
    private ExamDTO exam = new ExamDTO();
}
