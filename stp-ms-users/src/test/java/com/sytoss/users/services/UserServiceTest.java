package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import com.sytoss.users.model.ProfileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest extends AbstractJunitTest {

    @Spy
    private UserConnector userConnector;

    @Spy
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    protected void initSecurityContext() {
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "Luidji")
                .claim("family_name", "Monk").claim("userType", "teacher").claim("email", "test@email.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void shouldSaveTeacher() {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(1L);
        dto.setEmail("test@email.com");
        dto.setFirstName("Luidji");
        dto.setLastName("Monk");

        when(userConnector.getByEmail(anyString())).thenReturn(null).thenReturn(dto);
        when(userConnector.save(any(UserDTO.class))).thenReturn(dto);

        AbstractUser result = userService.getOrCreateUser("test@email.com");
        assertEquals(1L, result.getId());
        assertEquals("Luidji", result.getFirstName());
        assertEquals("Monk", result.getLastName());
        assertEquals("test@email.com", result.getEmail());
    }

    @Test
    public void testUpdatePhoto() {
        TeacherDTO teacherDTO = new TeacherDTO();
        when(userConnector.getByEmail("test@email.com")).thenReturn(teacherDTO);

        byte[] photoBytes = {0x01, 0x02, 0x03};
        MultipartFile photo = new MockMultipartFile("photo.jpg", photoBytes);

        userService.updatePhoto(photo);
        assertArrayEquals(photoBytes, teacherDTO.getPhoto());
        assertFalse(teacherDTO.isModerated());
    }

    @Test
    public void shouldUpdateProfile() {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(1L);
        dto.setEmail("test@email.com");
        dto.setFirstName("Luidji");
        dto.setLastName("Monk");

        when(userConnector.getByEmail(anyString())).thenReturn(dto);

        ProfileModel model = new ProfileModel();
        model.setPrimaryGroup(new Group());
        model.getPrimaryGroup().setName("PM-93-2");
        userService.updateProfile(model);
        verify(userConnector, times(1)).save(any(TeacherDTO.class));
    }

    @Test
    public void shouldReturnStudentWithTrueValidFlag() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Do");
        studentDTO.setEmail("test@test.com");
        studentDTO.setPrimaryGroup(groupDTO);
        studentDTO.setGroups(List.of(groupDTO));
        byte[] photoBytes = {0x01, 0x02, 0x03};
        studentDTO.setPhoto(photoBytes);
        when(userConnector.getByEmail("test@test.com")).thenReturn(studentDTO);
        assertTrue(userService.getOrCreateUser("test@test.com").isValid());
    }

    @Test
    public void shouldReturnStudentWithFalseValidFlag() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail("test@test.com");
        when(userConnector.getByEmail("test@test.com")).thenReturn(studentDTO);
        assertFalse(userService.getOrCreateUser("test@test.com").isValid());
    }

    @Test
    public void shouldReturnTeacherWithTrueValidFlag() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName("John");
        teacherDTO.setLastName("Do");
        teacherDTO.setEmail("test@test.com");
        byte[] photoBytes = {0x01, 0x02, 0x03};
        teacherDTO.setPhoto(photoBytes);
        when(userConnector.getByEmail("test@test.com")).thenReturn(teacherDTO);
        assertTrue(userService.getOrCreateUser("test@test.com").isValid());
    }

    @Test
    public void shouldReturnTeacherWithFalseValidFlag() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setEmail("test@test.com");
        when(userConnector.getByEmail("test@test.com")).thenReturn(teacherDTO);
        assertFalse(userService.getOrCreateUser("test@test.com").isValid());
    }
}
