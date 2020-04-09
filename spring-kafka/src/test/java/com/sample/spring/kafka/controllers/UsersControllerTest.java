package com.sample.spring.kafka.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.kafka.BaseControllerTest;
import com.sample.spring.kafka.dtos.RegisterUserDto;
import com.sample.spring.kafka.dtos.UserDto;
import com.sample.spring.kafka.entities.security.Role;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersControllerTest extends BaseControllerTest {

    @Test
    public void userShouldBeRegistered() throws JsonProcessingException, InterruptedException {
        RegisterUserDto dto = RegisterUserDto.builder()
                .email("test55@test.com")
                .firstName("Jora")
                .lastName("Gogi")
                .role(Role.Type.USER)
                .password("secret123")
                .build();

        given()
                .when()
                .contentType(ContentType.JSON)
                .header(RIGHT_HEADER)
                .cookie(ADMIN_RIGHT_AUTHORIZATION_COOKIE)
                .body(objectMapper.writeValueAsString(dto))
                .post("/users/registration")
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .body("email", equalTo("test55@test.com"))
                .body("first_name", equalTo("Jora"))
                .body("last_name", equalTo("Gogi"));

        Object value = queueByTopic.get("logs.user.registered").poll(5, TimeUnit.SECONDS);
        assertNotNull(value);
        UserDto userDto = new ObjectMapper().readValue(value.toString(), UserDto.class);
        assertNotNull(userDto.getId());
        assertEquals("test55@test.com", userDto.getEmail());
        assertEquals("Jora", userDto.getFirstName());
        assertEquals("Gogi", userDto.getLastName());
    }

    @Test
    public void userRegistrationIsNotAllowedWithoutPermissions() throws JsonProcessingException {
        RegisterUserDto dto = RegisterUserDto.builder()
                .email("test55@test.com")
                .firstName("Jora")
                .lastName("Gogi")
                .role(Role.Type.USER)
                .password("secret123")
                .build();

        given()
                .when()
                .contentType(ContentType.JSON)
                .header(RIGHT_HEADER)
                .cookie(USER_RIGHT_AUTHORIZATION_COOKIE)
                .body(objectMapper.writeValueAsString(dto))
                .post("/users/registration")
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void userRegistrationShouldBeValidated() throws JsonProcessingException {
        RegisterUserDto dto = RegisterUserDto.builder()
                .firstName("Jora")
                .lastName("Gogi")
                .role(Role.Type.USER)
                .password("secret123")
                .build();

        given()
                .when()
                .contentType(ContentType.JSON)
                .header(RIGHT_HEADER)
                .cookie(ADMIN_RIGHT_AUTHORIZATION_COOKIE)
                .body(objectMapper.writeValueAsString(dto))
                .post("/users/registration")
                .then()
                .statusCode(SC_UNPROCESSABLE_ENTITY);
    }
}
