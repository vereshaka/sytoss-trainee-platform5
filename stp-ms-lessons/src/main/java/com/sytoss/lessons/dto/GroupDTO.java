package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "STUDENT_GROUP")
public class GroupDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_group_id_generator")
    @SequenceGenerator(name = "student_group_id_generator", sequenceName = "STUDENT_GROUP_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;
}
