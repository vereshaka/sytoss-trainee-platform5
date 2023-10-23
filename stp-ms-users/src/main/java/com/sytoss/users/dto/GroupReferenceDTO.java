package com.sytoss.users.dto;


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
@Entity(name = "group2student")
@IdClass(GroupReferencePK.class)
public class GroupReferenceDTO extends Auditable{

    @Id
    @Column(name = "GROUP_ID")
    private Long groupId;

    @Id
    @ManyToOne
    @JsonIgnore
    private StudentDTO student;
}

