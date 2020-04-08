package com.sample.spring.security.auth.tokens;

import com.sample.spring.security.entities.security.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppToken {

    private UUID userId;

    private String fullName;
    private String email;
    private String xsrfToken;
    private Set<Permission.Type> permissions;
}
