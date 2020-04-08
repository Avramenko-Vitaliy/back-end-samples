package com.sample.spring.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.tests.auth.tokens.AppToken;
import com.sample.spring.tests.entities.security.Permission;
import com.sample.spring.tests.entities.security.Role;
import com.sample.spring.tests.repositories.PermissionsRepository;
import com.sample.spring.tests.services.JwtService;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CommonsLog
@ExtendWith(SpringExtension.class)
@SqlGroup({@Sql("classpath:test-clean.sql"), @Sql})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringTestsApplication.class})
public abstract class BaseControllerTest {

    protected static final UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    protected static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    protected static final UUID AUTH_ID = UUID.randomUUID();
    protected static final String JWT_TOKEN_COOKIE = "jwt_token_cookie";

    @SpyBean
    protected JwtService jwtService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected PermissionsRepository permissionsRepository;
    @LocalServerPort
    private Integer port;

    private Date EXPIRY_DATE;

    protected Cookie ADMIN_RIGHT_AUTHORIZATION_COOKIE;
    protected Cookie USER_RIGHT_AUTHORIZATION_COOKIE;
    protected Header RIGHT_HEADER;
    protected Header WRONG_JWT_HEADER;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        EXPIRY_DATE = Date.from(Instant.now().plus(12, ChronoUnit.HOURS));

        ADMIN_RIGHT_AUTHORIZATION_COOKIE = createUserCookie(createUserToken(ADMIN_ID, Role.Type.ADMIN));
        USER_RIGHT_AUTHORIZATION_COOKIE = createUserCookie(createUserToken(USER_ID, Role.Type.USER));

        RIGHT_HEADER = new Header("X-XSRF-TOKEN", AUTH_ID.toString());
        WRONG_JWT_HEADER = new Header("X-XSRF-TOKEN", UUID.randomUUID().toString());
    }

    @SneakyThrows
    private String createUserToken(UUID userId, Role.Type role) {
        return jwtService.buildToken(
                AppToken.builder()
                        .userId(userId)
                        .email("test@test.com")
                        .permissions(getRolePermissions(role))
                        .xsrfToken(AUTH_ID.toString())
                        .build()
        );
    }

    private Set<Permission.Type> getRolePermissions(Role.Type role) {
        return permissionsRepository.findPermissionsByRoleId(role.getId()).stream()
                .map(Permission::getApiKey)
                .collect(Collectors.toSet());
    }

    private Cookie createUserCookie(String jwtToken) {
        return new Cookie.Builder(JWT_TOKEN_COOKIE, jwtToken)
                .setHttpOnly(true)
                .setExpiryDate(EXPIRY_DATE)
                .build();
    }
}
