package com.test.ecommerce.user.controller;

import com.test.ecommerce.user.dto.LoginRequest;
import com.test.ecommerce.user.model.User;
import com.test.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public User register( @RequestBody User user) {
        return service.register(user);
    }


    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return service.login(request.getEmail(), request.getPassword());
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUser(id);
    }
}