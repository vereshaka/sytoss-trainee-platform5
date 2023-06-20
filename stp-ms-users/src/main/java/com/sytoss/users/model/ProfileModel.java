package com.sytoss.users.model;

import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileModel {

    private String firstName;

    private String lastName;

    private Group primaryGroup;
}
