package com.sytoss.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "GROUPS")
public class GroupDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_id_generator")
    @SequenceGenerator(name = "groups_id_generator", sequenceName = "GROUPS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

}
