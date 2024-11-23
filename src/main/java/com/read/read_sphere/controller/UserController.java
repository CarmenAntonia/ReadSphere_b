package com.read.read_sphere.controller;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.read.read_sphere.DTOs.LoginUserDTO;
import com.read.read_sphere.model.User;
import com.read.read_sphere.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if(user.getName().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Registration failed: All fields are required");
        }

        userServices.CreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody LoginUserDTO user, HttpSession session){
        if(userServices.LogIn(user,session)) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }

    @GetMapping("/userId")
    public Long getUserId(HttpSession session){
        return (Long) session.getAttribute("userId");
    }
}
