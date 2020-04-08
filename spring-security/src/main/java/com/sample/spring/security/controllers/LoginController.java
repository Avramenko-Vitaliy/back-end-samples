package com.sample.spring.security.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.spring.security.auth.tokens.AppToken;
import com.sample.spring.security.dtos.Credentials;
import com.sample.spring.security.services.JwtService;
import com.sample.spring.security.services.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class LoginController {

    private final JwtService jwtService;
    private final LoginService loginService;

    @PostMapping("/login")
    public AppToken login(@Validated @RequestBody Credentials credentials, HttpServletResponse response) throws JsonProcessingException {
        AppToken token = loginService.login(credentials);
        Cookie cookie = createCookie("jwt_token_cookie", jwtService.buildToken(token));
        response.addCookie(cookie);
        return token;
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = createCookie("jwt_token_cookie", "deleteAll");
        response.addCookie(cookie);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain("");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}
