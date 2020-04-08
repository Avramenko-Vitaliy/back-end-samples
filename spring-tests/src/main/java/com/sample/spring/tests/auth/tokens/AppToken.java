package com.sample.spring.tests.auth.tokens;

import com.sample.spring.tests.entities.security.Permission;
import com.sample.spring.tests.entities.security.Role;
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
    private Role.Type role;
    private String xsrfToken;
    private Set<Permission.Type> permissions;
}
