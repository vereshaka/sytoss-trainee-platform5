package com.sytoss.users.dto;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class TeacherDTO extends UserDTO {
}
