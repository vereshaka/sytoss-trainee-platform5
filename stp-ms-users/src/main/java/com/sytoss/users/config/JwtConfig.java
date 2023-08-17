package com.sytoss.users.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.net.MalformedURLException;
import java.net.URL;

import static com.sytoss.common.SSLUtil.disableSSL;

@Configuration
public class JwtConfig {

    static {
        disableSSL();
    }

    @Bean
    public JwtDecoder jwtDecoder(final OAuth2ResourceServerProperties properties) throws MalformedURLException {
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        RemoteJWKSet<SecurityContext> securityContextRemoteJWKSet = new RemoteJWKSet<>(new URL(properties.getJwt().getJwkSetUri()), new DefaultResourceRetriever());
        JWSVerificationKeySelector rs256 = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, securityContextRemoteJWKSet);
        jwtProcessor.setJWSKeySelector(rs256);
        NimbusJwtDecoder nimbusJwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        return nimbusJwtDecoder;
    }

}
