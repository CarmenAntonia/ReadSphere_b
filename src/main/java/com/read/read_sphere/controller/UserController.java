package com.read.read_sphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;

@RestController
public class UserController {
    @PostMapping("/")
    public String merge() {
        return "Hello World";
    }
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @PostMapping("/users")
    public User addOneUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }
}