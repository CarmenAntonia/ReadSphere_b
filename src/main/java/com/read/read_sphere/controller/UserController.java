package com.read.read_sphere.controller;

import com.read.read_sphere.DTOs.LoginUserDTO;
import com.read.read_sphere.DTOs.UserDTO;
import com.read.read_sphere.model.User;
import com.read.read_sphere.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId){
        var user = userServices.getUserById(userId);
        if(user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PatchMapping("/user/userBio/{userId}")
    public ResponseEntity<UserDTO> updateUserBio(@RequestBody String bio, @PathVariable("userId") Long userId){
        var user = userServices.updateUserBio(userId, bio);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PatchMapping("/user/userName/{userId}")
    public ResponseEntity<UserDTO> updateUserName(@RequestBody String name, @PathVariable("userId") Long userId){
        var user = userServices.updateUserName(userId, name);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
