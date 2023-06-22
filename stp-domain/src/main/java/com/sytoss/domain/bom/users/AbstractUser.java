package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonKey;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public abstract class AbstractUser {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private byte[] photo;

    private boolean isModerated;

    public boolean isValid() {
        return StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(firstName) && StringUtils.isNotEmpty(lastName) && ObjectUtils.isNotEmpty(photo);
    }

}
