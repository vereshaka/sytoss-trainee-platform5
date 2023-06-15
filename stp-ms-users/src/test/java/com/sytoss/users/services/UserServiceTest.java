package com.sytoss.users.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.TeacherDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest extends AbstractJunitTest {

    @Mock
    private TeacherConnector teacherConnector;

    @InjectMocks
    private UserService userService;

    @Spy
    private TeacherConvertor teacherConvertor = new TeacherConvertor();

    @Test
    public void shouldSaveTeacher() {
        Mockito.doAnswer((Answer<TeacherDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TeacherDTO result = (TeacherDTO) args[0];
            result.setId(1L);
            return result;
        }).when(teacherConnector).save(any(TeacherDTO.class));

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("firstName", "Luidji")
                .claim("lastName", "Monk").claim("middleName", "Hoki").claim("userType", "teacher").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AbstractUser result = userService.getOrCreateUser("test@email.com");
        assertEquals(1L, result.getId());
        assertEquals("Luidji", result.getFirstName());
        assertEquals("Monk", result.getLastName());
        assertEquals("Hoki", result.getMiddleName());
        assertEquals("test@email.com", result.getEmail());
    }
}