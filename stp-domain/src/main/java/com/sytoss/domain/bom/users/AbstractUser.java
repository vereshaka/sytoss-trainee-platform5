package com.sytoss.domain.bom.users;

import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractUser {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof AbstractUser)) return false;
        AbstractUser abstractUser = (AbstractUser) object;
        return abstractUser.getId() == id && (firstName == abstractUser.getFirstName() || firstName != null && firstName.equals(abstractUser.getFirstName()))
                && (middleName == abstractUser.getMiddleName() || middleName != null && middleName.equals(abstractUser.getMiddleName()))
                && (lastName == abstractUser.getLastName() || lastName != null && lastName.equals(abstractUser.getLastName()))
                && (email == abstractUser.getEmail() || email != null && email.equals(abstractUser.getEmail()));
    }
}
