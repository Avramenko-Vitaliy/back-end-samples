package com.sample.spring.kafka.controllers;

import com.sample.spring.kafka.configurations.KafkaTopics;
import com.sample.spring.kafka.controllers.params.Params;
import com.sample.spring.kafka.dtos.Pagination;
import com.sample.spring.kafka.dtos.RegisterUserDto;
import com.sample.spring.kafka.dtos.UserDto;
import com.sample.spring.kafka.publisher.PublishToKafka;
import com.sample.spring.kafka.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public UserDto getUser(@PathVariable("id") UUID id) {
        return usersService.getUserById(id);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public Pagination<UserDto> getUsers(@ModelAttribute Params params) {
        return usersService.getUsers(params);
    }

    @PostMapping("/users/registration")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @PublishToKafka(KafkaTopics.LOGS_USER_REGISTERED)
    public UserDto registration(@Validated @RequestBody RegisterUserDto dto) {
        return usersService.registration(dto);
    }
}
