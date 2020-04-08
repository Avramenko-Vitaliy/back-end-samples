package com.sample.spring.tests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.spring.tests.BaseControllerTest;
import com.sample.spring.tests.dtos.RegisterUserDto;
import com.sample.spring.tests.entities.security.Role;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UsersControllerTest extends BaseControllerTest {

    @Test
    public void userShouldBeRegistered() throws JsonProcessingException {
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
