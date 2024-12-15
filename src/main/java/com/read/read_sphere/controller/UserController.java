package com.read.read_sphere.controller;

import com.read.read_sphere.DTOs.LoginUserDTO;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;
import com.read.read_sphere.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "${frontend.redirect-url}", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class UserController {
    private final UserServices userServices;
    private final UserRepository userRepository;

    public UserController(UserServices userServices, UserRepository userRepository) {
        this.userServices = userServices;
        this.userRepository = userRepository;
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
    public ResponseEntity<?> logIn(@RequestBody LoginUserDTO user, HttpSession session) {
        if (userServices.LogIn(user, session)) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }

    @GetMapping("/userId")
    public Long getUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/userName")
    public ResponseEntity<?> getUserName(HttpSession session) {
        return handleUserSession(session, (user) ->
                ResponseEntity.ok(Collections.singletonMap("userName", user.getName()))
        );
    }

    @GetMapping("/user/bio")
    public ResponseEntity<Map<String, String>> getUserBio(@RequestParam("userId") Long userId) {
        String bio = userServices.getBio(userId);
        if (bio != null) {
            Map<String, String> response = new HashMap<>();
            response.put("bio", bio);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/bio")
    public ResponseEntity<String> updateUserBio(@RequestParam("userId") Long userId, @RequestBody User bioRequest) {
        boolean isUpdated = userServices.updateBio(userId, bioRequest.getBio());
        if (isUpdated) {
            return ResponseEntity.ok("Bio updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update bio");
        }
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/profilePicture")
    public ResponseEntity<?> getUserProfilePicture(HttpSession session) {
        return handleUserSession(session, (user) -> {
            String profilePicture = user.getProfilePicture();
            if (profilePicture != null && !profilePicture.isEmpty()) {
                return ResponseEntity.ok(profilePicture);
            } else {
                return ResponseEntity.ok("https://default-avatar-url.com/default-avatar.png");
            }
        });
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String password,
                                        @RequestParam(required = false) String bio,
                                        HttpSession session) {
        return new ResponseEntity<>(userServices.UpdateUser(userId, name, email, bio, password, session), HttpStatus.OK);
    }

    // Utility Methods to Eliminate Duplication
    private ResponseEntity<?> handleUserSession(HttpSession session, java.util.function.Function<User, ResponseEntity<?>> handler) {
        Long userId = (Long) session.getAttribute("userId");
        Object sessionToken = session.getAttribute("token");

        // Check for both userId and token in the session
        if (userId == null || sessionToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in or token missing");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Delegate specific logic to the provided handler
        return handler.apply(user);
    }
}

