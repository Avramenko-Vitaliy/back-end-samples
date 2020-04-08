package com.sample.spring.db.converters;

import com.sample.spring.db.dtos.UserDto;
import com.sample.spring.db.entities.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsersConverter {

    public UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    public User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }
}
