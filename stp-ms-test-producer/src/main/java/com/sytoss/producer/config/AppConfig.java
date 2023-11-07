package com.sytoss.producer.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.producer.connectors.UserConnector;
import com.sytoss.producer.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.sytoss.common.SSLUtil.disableSSL;

@Configuration
@Slf4j
@EnableScheduling
public class AppConfig {

    static {
        disableSSL();
    }

    @Autowired
    private UserConnector userConnector;
    @Autowired
    private UserConvertor userConvertor;

    @Bean
    public JwtDecoder jwtDecoder(final OAuth2ResourceServerProperties properties) throws MalformedURLException {
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        RemoteJWKSet<SecurityContext> securityContextRemoteJWKSet = new RemoteJWKSet<>(new URL(properties.getJwt().getJwkSetUri()), new DefaultResourceRetriever());
        JWSVerificationKeySelector rs256 = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, securityContextRemoteJWKSet);
        jwtProcessor.setJWSKeySelector(rs256);
        NimbusJwtDecoder jwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        jwtDecoder.setClaimSetConverter(new Converter<>() {

            private final MappedJwtClaimSetConverter delegate =
                    MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

            public Map<String, Object> convert(Map<String, Object> claims) {
                Map<String, Object> convertedClaims = this.delegate.convert(claims);
                try {
                    Map<String, Object> user = (Map<String, Object>) userConnector.getMyProfile();
                    if (!user.containsKey("primaryGroup")) {
                        Teacher teacher = new Teacher();
                        userConvertor.toTeacher(user, teacher);
                        convertedClaims.put("user", teacher);
                    } else {
                        Student student = new Student();
                        userConvertor.toStudent(user, student);
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

    @Bean
    public PumlConvertor pumlConvertor() {
        return new PumlConvertor();
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {

        return new MongoCustomConversions(
                Arrays.asList(
                        new JavaDateToSqlTimestampConverter(),
                        new JavaDateToSqlDateConverter()));
    }
}
