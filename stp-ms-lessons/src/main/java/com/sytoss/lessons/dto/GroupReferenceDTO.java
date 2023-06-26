package com.sytoss.lessons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "group2discipline")
@IdClass(GroupReferencePK.class)
public class GroupReferenceDTO {

    @Id
    @Column(name = "GROUP_ID")
    private Long groupId;

    @Id
    @ManyToOne
    @JsonIgnore
    private DisciplineDTO discipline;
}
