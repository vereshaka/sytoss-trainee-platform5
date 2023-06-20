package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import com.sytoss.users.model.ProfileModel;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public void toDTO(Student source, StudentDTO destination) {
        toDTO(source, (UserDTO) destination);
        destination.setPrimaryGroupId(source.getPrimaryGroup().getId());
    }

    public void fromDTO(StudentDTO source, Student destination) {
        fromDTO((UserDTO) source, destination);
        destination.setPrimaryGroup(new Group());
        destination.getPrimaryGroup().setId(source.getId());
    }

    public void fromDTO(UserDTO source, AbstractUser destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());
        destination.setModerated(source.isModerated());
        destination.setPhoto(source.getPhoto());
    }

    public void fromDTO(Jwt source, AbstractUser destination) {
        destination.setEmail(source.getClaim("email"));
        destination.setFirstName(source.getClaim("given_name"));
        destination.setLastName(source.getClaim("family_name"));
    }

    public void toDTO(AbstractUser source, UserDTO destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());
        destination.setModerated(source.isModerated());
        destination.setPhoto(source.getPhoto());
    }
}
