package com.sytoss.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("S")
@Getter
@Setter
public class StudentDTO extends UserDTO {

    @Column(name = "GROUP_ID")
    private Long primaryGroupId;
}
