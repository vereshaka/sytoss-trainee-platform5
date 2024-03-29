package com.sytoss.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "APP_USER")
@DiscriminatorColumn(name = "USER_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class UserDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "APP_USER_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHOTO")
    private byte[] photo;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "IS_MODERATED")
    private boolean isModerated;

    @Column(name = "UID")
    private String uid;
}
