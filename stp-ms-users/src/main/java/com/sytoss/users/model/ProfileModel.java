package com.sytoss.users.model;

import com.sytoss.domain.bom.users.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileModel {

    private String firstName;

    private String middleName;

    private String lastName;

    private Group primaryGroup;

    private MultipartFile photo;
}
