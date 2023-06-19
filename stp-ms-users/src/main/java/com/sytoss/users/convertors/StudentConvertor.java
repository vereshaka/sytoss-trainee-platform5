package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.dto.StudentDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class StudentConvertor extends AbstractUserConveror{

    public void toDTO(Student source, StudentDTO destination) {
        super.toDTO(source, destination);
        //TODO: yevgenyv: implement me: destination.setPrimaryGroupId(source.getPri());
    }

    public void fromDTO(StudentDTO source, Student destination) {
        super.fromDTO(source, destination);
        //TODO: yevgenyv: implement me: destination.setPrimaryGroup(source.getPri());
    }

}
