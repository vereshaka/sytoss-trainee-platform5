package com.sytoss.users;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
@Getter
public abstract class AbstractApplicationTest extends AbstractJunitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AbstractApplicationContext applicationContext;

    @Getter
    private static RSAKey jwk;

    @Getter
    private static HttpServer httpServer;

    @Test
    public void shouldLoadApplicationContext() {
        assertNotNull(applicationContext);
    }

    protected long getPort() {
        return ((AnnotationConfigServletWebServerApplicationContext) applicationContext).getWebServer().getPort();
    }

    protected URI getEndpoint(String uriPath) {
        try {
            return new URI("http://localhost:" + getPort() + uriPath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopServer() {
        httpServer.stop(0);
    }

    public static void setupJWK() throws JOSEException, IOException {
        jwk = new RSAKeyGenerator(2048)
                .keyID("1234")
                .generate();

        String publicKey = "{\n" +
                "  \"keys\": [" +
                jwk.toPublicJWK().toJSONString() + "]\n" +
                "}";

        httpServer = HttpServer.create(new InetSocketAddress(9030), 0);
        httpServer.createContext("/realms/traineeplatform/protocol/openid-connect/certs", exchange -> {
            byte[] response = publicKey.getBytes();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        httpServer.start();
    }

    protected String generateJWT(List<String> roles) throws JOSEException {
        LinkedTreeMap<String, ArrayList<String>> realmAccess = new LinkedTreeMap<>();
        realmAccess.put("roles", new ArrayList<String>(roles));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("test")
                .issuer("test@test")
                .claim("realm_access", realmAccess)
                .claim("email", "test@test")
                .claim("firstName", "Ivan")
                .claim("lastName", "Ivanovich")
                .claim("middleName", "Ivanov")
                .expirationTime(new Date(new Date().getTime() + 60 * 100000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(new JOSEObjectType("jwt")).keyID("1234").build(),
                claimsSet);

        signedJWT.sign(new RSASSASigner(AbstractApplicationTest.getJwk()));

        return signedJWT.serialize();
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (requestEntity instanceof HttpEntity<?>) {
            return restTemplate.exchange(getEndpoint(uri), method, (HttpEntity<?>) requestEntity, responseType);
        } else {
            HttpEntity<?> request = new HttpEntity<>(requestEntity, headers);
            return restTemplate.exchange(getEndpoint(uri), method, request, responseType);
        }
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (requestEntity instanceof HttpEntity<?>) {
            return restTemplate.exchange(getEndpoint(uri), method, (HttpEntity<?>) requestEntity, responseType);
        } else {
            HttpEntity<?> request = new HttpEntity<>(requestEntity, headers);
            return restTemplate.exchange(getEndpoint(uri), method, request, responseType);
        }
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, ParameterizedTypeReference<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, Object requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }
}
