package com.samples.spring.controllers;

import com.samples.spring.dtos.User;
import com.samples.spring.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") UUID id) {
        return usersService.getUserById(id);
    }
}
