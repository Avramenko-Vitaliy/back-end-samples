package com.sample.spring.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.kafka.auth.tokens.AppToken;
import com.sample.spring.kafka.config.KafkaTestConfig;
import com.sample.spring.kafka.entities.security.Permission;
import com.sample.spring.kafka.entities.security.Role;
import com.sample.spring.kafka.repositories.PermissionsRepository;
import com.sample.spring.kafka.services.JwtService;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

@CommonsLog
@ExtendWith(SpringExtension.class)
@SqlGroup({@Sql("classpath:test-clean.sql"), @Sql})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringKafkaApplication.class, KafkaTestConfig.class})
public abstract class BaseControllerTest {

    protected static final UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    protected static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    protected static final UUID AUTH_ID = UUID.randomUUID();
    protected static final String JWT_TOKEN_COOKIE = "jwt_token_cookie";

    protected static Map<String, BlockingDeque<Object>> queueByTopic = new HashMap<>();

    @SpyBean
    protected JwtService jwtService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected PermissionsRepository permissionsRepository;
    @LocalServerPort
    private Integer port;
    @Autowired
    protected List<NewTopic> topics;

    private Date EXPIRY_DATE;

    protected Cookie ADMIN_RIGHT_AUTHORIZATION_COOKIE;
    protected Cookie USER_RIGHT_AUTHORIZATION_COOKIE;
    protected Header RIGHT_HEADER;
    protected Header WRONG_JWT_HEADER;

    /**
     * Kafka
     */
    public static final KafkaContainer kafka = new KafkaContainer();

    static {
        kafka.start();
        Locale.setDefault(Locale.US);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        EXPIRY_DATE = Date.from(Instant.now().plus(12, ChronoUnit.HOURS));

        ADMIN_RIGHT_AUTHORIZATION_COOKIE = createUserCookie(createUserToken(ADMIN_ID, Role.Type.ADMIN));
        USER_RIGHT_AUTHORIZATION_COOKIE = createUserCookie(createUserToken(USER_ID, Role.Type.USER));

        RIGHT_HEADER = new Header("X-XSRF-TOKEN", AUTH_ID.toString());
        WRONG_JWT_HEADER = new Header("X-XSRF-TOKEN", UUID.randomUUID().toString());

        queueByTopic = topics.stream().collect(Collectors.toMap(NewTopic::name, topic -> new LinkedBlockingDeque<>()));
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

    @KafkaListener(topicPattern = ".*")
    public void listener(ConsumerRecord<?, ?> record, @org.springframework.messaging.handler.annotation.Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Object value = record.value();
        log.info(String.format("\n\nBase listener: topic = %s, message = %s\n\n", topic, value));
        queueByTopic.get(topic).offer(value);
    }
}
