package com.sytoss.lessons.dto;

import com.sytoss.domain.bom.lessons.ConditionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TASK_CONDITION")
public class TaskConditionDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_condition_id_generator")
    @SequenceGenerator(name = "task_condition_id_generator", sequenceName = "TASK_CONDITION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ConditionType type;
}