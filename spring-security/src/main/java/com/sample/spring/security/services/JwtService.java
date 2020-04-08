package com.sample.spring.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.security.auth.tokens.AppToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Service
@AllArgsConstructor
public class JwtService {

    private final ObjectMapper objectMapper;

    public String buildToken(AppToken data) throws JsonProcessingException {
        Claims claims = new DefaultClaims();
        claims.put("auth_token", objectMapper.writeValueAsString(data));
        Date issued = new Date();
        return Jwts.builder()
                .setIssuedAt(issued)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "secret")
                .setExpiration(new Date(issued.getTime() + 12 * 3600000L))
                .compact();
    }

    public void checkToken(String token, String xsrf) throws JsonProcessingException {
        if (StringUtils.isEmpty(xsrf)) {
            throw new AuthorizationServiceException("Xsrf token is not set.");
        }
        AppToken appToken = parseToken(token);
        if (!Objects.equals(appToken.getXsrfToken(), xsrf)) {
            throw new AuthorizationServiceException("Xsrf token does not match.");
        }
    }

    public AppToken parseToken(String token) throws JsonProcessingException {
        Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
        String authTokenJson = (String)claims.get("auth_token");
        return objectMapper.readValue(authTokenJson, AppToken.class);
    }
}
