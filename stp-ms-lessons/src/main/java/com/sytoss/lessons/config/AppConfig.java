package com.sytoss.lessons.config;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.convertors.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Slf4j
public class AppConfig {

    @Autowired
    private UserConnector userConnector;
    @Autowired
    private UserConvertor userConvertor;

    @Bean
    public JwtDecoder jwtDecoder(final OAuth2ResourceServerProperties properties) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
                properties.getJwt().getJwkSetUri()).build();

        jwtDecoder.setClaimSetConverter(new Converter<Map<String, Object>, Map<String, Object>>() {

            private final MappedJwtClaimSetConverter delegate =
                    MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

            public Map<String, Object> convert(Map<String, Object> claims) {
                Map<String, Object> convertedClaims = this.delegate.convert(claims);
                try {
                    Object user = userConnector.getMyProfile();
                    Teacher teacher = new Teacher();
                    Student student = new Student();
                    userConvertor.toTeacherOrStudent((LinkedHashMap<String, Object>) user, teacher,student);
                    if(!student.isValid()){
                        convertedClaims.put("user", teacher);
                    }else {
                        convertedClaims.put("user", student);
                    }
                } catch (Exception e) {
                    log.error("Could not retrieve user details", e);
                }
                return convertedClaims;
            }
        });
        return jwtDecoder;
    }
}
