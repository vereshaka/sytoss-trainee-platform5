package com.sytoss.users.connectors;

import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentConnector extends JpaRepository<StudentDTO, Long> {

    StudentDTO getByEmail(String email);
}
