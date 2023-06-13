package com.sytoss.users.services;

import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentConnector studentConnector;

    public void updatePhoto(MultipartFile photo) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setModerated(false);
        byte[] photoBytes;
        try {
            photoBytes = photo.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentDTO.setPhoto(photoBytes);
        studentConnector.save(studentDTO);
    }
}