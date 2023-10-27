package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Setter
public class Student extends AbstractUser {

    @JsonView({PersonalExam.Public.class})
    private Group primaryGroup;

    @Override
    public boolean isValid() {
        return super.isValid() && ObjectUtils.isNotEmpty(primaryGroup);
    }

    @Override
    public String getType() {
        return "student";
    }
}
