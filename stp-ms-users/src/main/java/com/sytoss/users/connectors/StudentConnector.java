package com.sytoss.users.connectors;

import com.sytoss.users.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentConnector extends JpaRepository<StudentDTO, Long> {

    StudentDTO findByEmail(String email);
}
