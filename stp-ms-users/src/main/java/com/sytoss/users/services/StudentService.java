package com.sytoss.users.services;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.StudentConvertor;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentConnector studentConnector;

//    private final StudentConvertor studentConvertor;

    public ResponseEntity<String> updatePhoto(String email, MultipartFile photo) throws IOException {

        StudentDTO studentDTO = studentConnector.getByEmail(email);

        if (studentDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(photo.getOriginalFilename()));

        // Создание директории, если она не существует
        String uploadDir = "/path/to/upload/directory";

        // Создание директории, если она не существует
        Files.createDirectories(Path.of(uploadDir));

        // Полный путь к сохраняемому файлу
        String filePath = uploadDir + "/" + fileName;

        // Сохранение файла
        Files.copy(photo.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);

        studentDTO.setPhotoPath(filePath);

        studentConnector.save(studentDTO);

        return ResponseEntity.status(HttpStatus.OK).body("Photo updated successfully");
    }
}
