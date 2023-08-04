package com.sytoss.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("S")
@Getter
@Setter
public class StudentDTO extends UserDTO {

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    private GroupDTO primaryGroup;

    @ManyToMany
    @JoinTable(
            name = "GROUP2STUDENT",
            joinColumns = @JoinColumn(name = "STUDENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private List<GroupDTO> groups;

}
