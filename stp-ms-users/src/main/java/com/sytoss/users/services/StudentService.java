package com.sytoss.users.services;

import com.sytoss.domain.bom.exceptions.business.notfound.StudentNotFoundException;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.dto.StudentDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentConnector studentConnector;

    public MultipartFile updatePhoto(String email, MultipartFile photo) {

        try {
            StudentDTO studentDTO = studentConnector.getByEmail(email);
            studentDTO.setModerated(false);
            byte[] photoBytes;
            try {
                photoBytes = photo.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            studentDTO.setPhoto(photoBytes);
            studentConnector.save(studentDTO);
            return photo;
        } catch (EntityNotFoundException e) {
            throw new StudentNotFoundException(email);
        }
    }
}