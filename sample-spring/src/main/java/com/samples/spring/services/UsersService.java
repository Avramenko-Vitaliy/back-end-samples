package com.samples.spring.services;

import com.samples.spring.dtos.User;
import com.samples.spring.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UsersService {

    private static final Map<UUID, User> USERS_MAP_BY_ID = new HashMap<>();
    static {
        USERS_MAP_BY_ID.put(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                User.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                        .firstName("Gogi")
                        .lastName("Gogi")
                        .build()
        );
        USERS_MAP_BY_ID.put(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                User.builder()
                        .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                        .firstName("Jora")
                        .build()
        );
    }

    public User getUserById(UUID id) {
        User user = USERS_MAP_BY_ID.get(id);
        if (Objects.isNull(user)) {
            throw new UserNotFound("User not found!");
        }
        return user;
    }
}
