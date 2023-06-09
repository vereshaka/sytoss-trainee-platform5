package com.sytoss.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Entity(name = "STUDENT")
public class StudentDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_generator")
    @SequenceGenerator(name = "student_id_generator", sequenceName = "STUDENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHOTO")
    private String photo;

    @Column(name = "IS_MODERATED")
    boolean isModerated;
}
