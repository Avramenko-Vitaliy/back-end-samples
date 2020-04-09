package com.sample.spring.kafka.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.spring.kafka.BaseControllerTest;
import com.sample.spring.kafka.auth.tokens.AppToken;
import io.restassured.http.Cookie;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SqlGroup(@Sql(value = "classpath:test-clean.sql"))
public class CommonControllerTest extends BaseControllerTest {

    @Test
    public void testToken() throws JsonProcessingException {
        given()
                .when()
                .get("/sample-api/users")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token is not set."));

        given()
                .when()
                .cookie(ADMIN_RIGHT_AUTHORIZATION_COOKIE)
                .get("/sample-api/users")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Xsrf token is not set."));

        given()
                .when()
                .header(WRONG_JWT_HEADER)
                .cookie(ADMIN_RIGHT_AUTHORIZATION_COOKIE)
                .get("/sample-api/users")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Xsrf token does not match."));

        Cookie tokenCookie = new Cookie.Builder(
                "jwt_token_cookie",
                "eyJhbGciOiJIUzUxMiJ9.eyJhdXRoX3Rva2VuIjoie1widXNlcl9pZFwiOl" +
                        "wiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAxXCIsXCJ" +
                        "mdWxsX25hbWVcIjpcIkpvaG4gRG9lXCIsXCJlbWFpbFwiOlwidGVzdEB0Z" +
                        "XN0LmNvbVwiLFwieHNyZl90b2tlblwiOlwiNjE1YWU0MzEtODY1MC00MT" +
                        "U2LWIzMjktMTkwZGNhZjRmOWFhXCIsXCJwZXJtaXNzaW9uc1wiOltcIk1B" +
                        "TkFHRV9VU0VSU1wiLFwiVklFV19VU0VSU1wiXX0iLCJleHAiOjE1ODY0MTM" +
                        "yOTJ9.P7YtnzIQ8ywpJYsrbBKi08coHdgvd2zeQwNyRlJ8v6B1fW7Skv4n8_"
        ).build();

        given()
                .when()
                .cookie(tokenCookie)
                .header(RIGHT_HEADER)
                .get("/sample-api/users")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Invalid token."));

        Date expireDate = Date.from(Instant.now().minus(12, ChronoUnit.HOURS));
        String token = jwtService.buildToken(
                AppToken.builder()
                        .userId(ADMIN_ID)
                        .xsrfToken(AUTH_ID.toString())
                        .build(),
                expireDate
        );

        tokenCookie = new Cookie.Builder("jwt_token_cookie", token).build();
        given()
                .when()
                .header(RIGHT_HEADER)
                .cookie(tokenCookie)
                .get("/sample-api/users")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo("Token is expired."));
    }
}
