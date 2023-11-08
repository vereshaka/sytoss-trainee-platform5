package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public abstract class AbstractUser {

    private Long id;

    @JsonView({PersonalExam.Public.class})
    private String firstName;

    private String middleName;

    @JsonView({PersonalExam.Public.class})
    private String lastName;

    private String email;

    private String imageName;

    private boolean isModerated;

    @JsonView({PersonalExam.Public.class})
    private String uid;

    public boolean isValid() {
        return StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName) && ObjectUtils.isNotEmpty(imageName);
    }

    public abstract String getType();

    public static class Public {}
}
