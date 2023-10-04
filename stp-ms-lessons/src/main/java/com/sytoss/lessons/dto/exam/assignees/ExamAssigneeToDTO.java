package com.sytoss.lessons.dto.exam.assignees;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "EXAM_ASSIGNEE_TO")
@DiscriminatorColumn(name = "ASSIGNEE_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ExamAssigneeToDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_assignee_to_id_generator")
    @SequenceGenerator(name = "exam_assignee_to_id_generator", sequenceName = "EXAM_ASSIGNEE_TO_SEQ", allocationSize = 1)
    private Long id;
}
