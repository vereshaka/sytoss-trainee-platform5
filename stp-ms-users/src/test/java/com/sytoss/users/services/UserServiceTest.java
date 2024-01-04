package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.users.connectors.ExamAssigneeConnector;
import com.sytoss.users.connectors.ImageProviderConnector;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest extends StpUnitTest {

    @Mock
    private UserConnector userConnector;

    @Mock
    private ImageProviderConnector imageProviderConnector;

    @Mock
    private ExamAssigneeConnector examAssigneeConnector;

    @Spy
    private UserConverter userConverter;

    @Spy
    private GroupConvertor groupConvertor;

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
        ReflectionTestUtils.setField(userService, "isAllowed", true);
        TeacherDTO dto = new TeacherDTO();
        dto.setId(1L);
        dto.setEmail("test@email.com");
        dto.setFirstName("Luidji");
        dto.setMiddleName("Mock");
        dto.setLastName("Monk");

        when(userConnector.getByEmail(anyString())).thenReturn(null).thenReturn(dto);
        when(userConnector.save(any(UserDTO.class))).thenReturn(dto);

        AbstractUser result = userService.getOrCreateUser("test@email.com");
        assertEquals(1L, result.getId());
        assertEquals("Luidji", result.getFirstName());
        assertEquals("Mock", result.getMiddleName());
        assertEquals("Monk", result.getLastName());
        assertEquals("test@email.com", result.getEmail());
    }

    @Test
    public void shouldRetrieveMyDiscipline() {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        dto.setEmail("test@email.com");
        dto.setFirstName("Luidji");
        dto.setMiddleName("Mock");
        dto.setLastName("Monk");
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        dto.setGroups(List.of(groupDTO));

        when(userConnector.getByEmail(anyString())).thenReturn(dto);
        List<Long> result = userService.findGroupsId();
        assertEquals(1, result.size());
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
        dto.setMiddleName("Mock");
        dto.setLastName("Monk");

        when(userConnector.getByEmail(anyString())).thenReturn(dto);

        ProfileModel model = new ProfileModel();
        model.setPrimaryGroup(new Group());
        model.getPrimaryGroup().setName("PM-93-2");
        userService.updateProfile(model);
        verify(userConnector, times(1)).save(any(TeacherDTO.class));
    }

    @Test
    public void shouldReturnGroupsOfStudent() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setEmail("test@test.com");
        studentDTO.setFirstName("John");
        studentDTO.setMiddleName("Mock");
        studentDTO.setLastName("Do");
        studentDTO.setGroups(List.of(createGroupDTO("First Group"), createGroupDTO("Second Group"), createGroupDTO("Third Group")));
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("sub", "111-11-111").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userConnector.getByEmail("test@test.com")).thenReturn(studentDTO);
        List<Group> groups = userService.findByStudent();
        assertEquals(3, groups.size());
    }

    @Test
    public void shouldReturnStudentNotFoundException() {
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userConnector.getByEmail("test@test.com")).thenThrow(new UserNotFoundException("User not found"));
        assertThrows(UserNotFoundException.class, () -> userService.findByStudent());
    }

    @Test
    public void shouldReturnStudentWithTrueValidFlag() {
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("sub", "111-11-111").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        groupDTO.setStudents(new ArrayList<>());
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setMiddleName("Mock");
        studentDTO.setLastName("Do");
        studentDTO.setEmail("test@test.com");
        studentDTO.setPrimaryGroup(groupDTO);
        studentDTO.setGroups(List.of(groupDTO));
        studentDTO.setImageName("Image Name");
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
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("sub", "111-11-111").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertFalse(userService.getOrCreateUser("test@test.com").isValid());
    }

    @Test
    public void shouldReturnTeacherWithTrueValidFlag() {
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("sub", "111-11-111").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName("John");
        teacherDTO.setMiddleName("Mock");
        teacherDTO.setLastName("Do");
        teacherDTO.setEmail("test@test.com");
        teacherDTO.setImageName("Image Name");
        byte[] photoBytes = {0x01, 0x02, 0x03};
        teacherDTO.setPhoto(photoBytes);
        when(userConnector.getByEmail("test@test.com")).thenReturn(teacherDTO);
        assertTrue(userService.getOrCreateUser("test@test.com").isValid());
    }

    @Test
    public void shouldReturnTeacherWithFalseValidFlag() {
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "John").claim("middle_name", "Mock")
                .claim("family_name", "Do").claim("sub", "111-11-111").claim("userType", "student").claim("email", "test@test.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setEmail("test@test.com");
        when(userConnector.getByEmail("test@test.com")).thenReturn(teacherDTO);
        assertFalse(userService.getOrCreateUser("test@test.com").isValid());
    }

    private GroupDTO createGroupDTO(String name) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(name);
        groupDTO.setStudents(new ArrayList<>());
        return groupDTO;
    }

    @Test
    public void testGetPhoto() {
        byte[] photoBytes = {0x01, 0x02, 0x03};
        StudentDTO dto = new StudentDTO();
        String userEmail = "test@email.com";
        dto.setId(1L);
        dto.setEmail(userEmail);
        dto.setFirstName("Luidji");
        dto.setMiddleName("Mock");
        dto.setLastName("Monk");
        dto.setPhoto(photoBytes);

        when(userConnector.getByEmail(userEmail)).thenReturn(dto);
        byte[] result = userService.getMyPhoto();
        assertEquals(photoBytes, result);
    }
}
