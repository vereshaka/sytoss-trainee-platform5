package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    private final GroupConvertor groupConvertor = new GroupConvertor();

    public void toDTO(Student source, StudentDTO destination) {
        toDTO(source, (UserDTO) destination);
        GroupDTO groupDTO = new GroupDTO();
        groupConvertor.toDTO(source.getPrimaryGroup(), groupDTO);
        destination.setPrimaryGroup(groupDTO);
    }

    public void fromDTO(StudentDTO source, Student destination) {
        fromDTO((UserDTO) source, destination);
        Group group = new Group();
        groupConvertor.fromDTO(source.getPrimaryGroup(), group);
        destination.setPrimaryGroup(group);
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
