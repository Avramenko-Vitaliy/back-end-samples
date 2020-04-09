package com.sample.spring.kafka.converters;

import com.sample.spring.kafka.dtos.RegisterUserDto;
import com.sample.spring.kafka.dtos.UserDto;
import com.sample.spring.kafka.entities.User;
import com.sample.spring.kafka.entities.security.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsersConverter {

    public UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    public User toEntity(RegisterUserDto dto) {
        Role role = dto.getRole().getInstance();
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(role)
                .roleId(role.getId())
                .build();
    }
}
