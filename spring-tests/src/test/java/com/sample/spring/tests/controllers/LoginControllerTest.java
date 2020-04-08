package com.sample.spring.tests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.spring.tests.BaseControllerTest;
import com.sample.spring.tests.dtos.Credentials;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.detailedCookie;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class LoginControllerTest extends BaseControllerTest {

    @Test
    public void loginSuccess() throws JsonProcessingException {
        Credentials credentials = Credentials.builder()
                .email("test@test.com")
                .password("secret")
                .build();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(credentials))
                .post("/login")
                .then()
                .statusCode(SC_OK)
                .cookie(JWT_TOKEN_COOKIE)
                .body("email", equalTo("test@test.com"))
                .body("full_name", equalTo("John Doe"))
                .body("role", equalTo("ADMIN"))
                .body("permissions", hasSize(2));
    }

    @Test
    public void logout() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .header(RIGHT_HEADER)
                .cookie(ADMIN_RIGHT_AUTHORIZATION_COOKIE)
                .delete("/logout")
                .then()
                .statusCode(SC_OK)
                .cookie(JWT_TOKEN_COOKIE, detailedCookie().maxAge(0).value("deleteAll"));
    }
}
