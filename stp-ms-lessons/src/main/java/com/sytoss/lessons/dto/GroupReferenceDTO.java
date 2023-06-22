package com.sytoss.lessons.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
    @Column(name = "DISCIPLINE_ID")
    private Long disciplineId;
}
