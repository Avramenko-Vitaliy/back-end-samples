package com.sample.spring.tests.auth;

import com.sample.spring.tests.auth.tokens.AppToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static java.util.stream.Collectors.toSet;

public class UserAuthentication implements Authentication {

    private boolean authenticated = true;
    private final AppToken appToken;

    public UserAuthentication(AppToken appToken) {
        this.appToken = appToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appToken.getPermissions().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
    }

    @Override
    public Object getCredentials() {
        return appToken.getUserId();
    }

    @Override
    public Object getDetails() {
        return appToken;
    }

    @Override
    public Object getPrincipal() {
        return appToken.getUserId();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return appToken.getEmail();
    }
}
