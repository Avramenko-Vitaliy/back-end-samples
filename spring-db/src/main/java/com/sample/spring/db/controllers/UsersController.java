package com.sample.spring.db.controllers;

import com.sample.spring.db.controllers.params.Params;
import com.sample.spring.db.dtos.Pagination;
import com.sample.spring.db.dtos.UserDto;
import com.sample.spring.db.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable("id") UUID id) {
        return usersService.getUserById(id);
    }

    @GetMapping("/users")
    public Pagination<UserDto> getUsers(@ModelAttribute Params params) {
        return usersService.getUsers(params);
    }

    @PostMapping("/users")
    public UserDto createUser(@Validated @RequestBody UserDto dto) {
        return usersService.createUser(dto);
    }
}
