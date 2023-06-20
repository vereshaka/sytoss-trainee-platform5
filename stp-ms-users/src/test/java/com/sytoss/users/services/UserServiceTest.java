package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.StudentConverter;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends AbstractJunitTest {

    @Mock
    private UserConnector userConnector;

    @Spy
    private TeacherConvertor teacherConvertor = new TeacherConvertor();

    @Spy
    private StudentConverter studentConverter = new StudentConverter(new GroupConvertor());

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldSaveTeacher() {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(1L);
        dto.setEmail("test@email.com");
        dto.setFirstName("Luidji");
        dto.setLastName("Monk");
        when(userConnector.save(any(UserDTO.class))).thenReturn(dto);

        when(userConnector.getByEmail(anyString())).thenReturn(null).thenReturn(dto);

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "Luidji")
                .claim("family_name", "Monk").claim("userType", "teacher").claim("email", "test@email.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

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
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("given_name", "Luidji")
                .claim("family_name", "Monk").claim("userType", "teacher").claim("email", "test@email.com").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        userService.updatePhoto(photo);
        assertArrayEquals(photoBytes, teacherDTO.getPhoto());
        assertFalse(teacherDTO.isModerated());
    }
}
