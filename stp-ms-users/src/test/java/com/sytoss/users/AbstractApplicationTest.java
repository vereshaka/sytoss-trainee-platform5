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
import org.apache.commons.lang3.StringUtils;
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

    protected String generateJWT(List<String> roles, String firstName, String lastName, String email, String userType) {
        LinkedTreeMap<String, ArrayList<String>> realmAccess = new LinkedTreeMap<>();
        realmAccess.put("roles", new ArrayList<String>(roles));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("test")
                .issuer("test@test")
                .claim("realm_access", realmAccess)
                .claim("email", email)
                .claim("given_name", firstName)
                .claim("family_name", lastName)
                .claim("userType", userType)
                .expirationTime(new Date(new Date().getTime() + 60 * 100000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(new JOSEObjectType("jwt")).keyID("1234").build(),
                claimsSet);

        try {
            signedJWT.sign(new RSASSASigner(AbstractApplicationTest.getJwk()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(getEndpoint(uri), method, requestEntity, responseType);
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(getEndpoint(uri), method, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, HttpEntity<?> requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, HttpEntity<?> requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }

    protected HttpHeaders getDefaultHttpHeaders() {
        HttpHeaders result = new HttpHeaders();
        if (StringUtils.isNoneEmpty()) {
            result.setBearerAuth(getToken());
        }
        return result;
    }

    protected String getToken() {
        return null;
    }
}
