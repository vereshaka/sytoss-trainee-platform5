package com.sytoss.domain.bom.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class Student extends AbstractUser {

    private MultipartFile photo;
}
