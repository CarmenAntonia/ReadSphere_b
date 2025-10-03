package com.read.read_sphere.services;

import com.read.read_sphere.DTOs.LoginUserDTO;
import com.read.read_sphere.DTOs.UserDTO;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServices(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void CreateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean LogIn(LoginUserDTO user, HttpSession session) {
        User u = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (u == null)
            return false;

        if (passwordEncoder.matches(user.getPassword(), u.getPassword())){
            session.setAttribute("userId", u.getUserId());
            return true;
        }
        return false;
    }

    public UserDTO getUserById(long userId) {
        var user = userRepository.findById(userId);
        return user.map(value -> new UserDTO(value.getName(), value.getBio())).orElse(null);
    }

    public UserDTO updateUserBio(long userId, String newBio) {
        var user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setBio(newBio);
            userRepository.save(user);
            return new UserDTO(user.getName(), user.getBio());
        }
        return null;
    }

    public UserDTO updateUserName(long userId, String name) {
        var user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setName(name);
            userRepository.save(user);
            return new UserDTO(user.getName(), user.getBio());
        }
        return null;
    }
}
