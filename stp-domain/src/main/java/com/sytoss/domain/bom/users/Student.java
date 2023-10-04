package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Setter
public class Student extends AbstractUser {

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
