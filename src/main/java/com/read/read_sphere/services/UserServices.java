package com.read.read_sphere.services;

import com.read.read_sphere.model.Book;
import com.read.read_sphere.model.UserBookshelf;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.read.read_sphere.DTOs.LoginUserDTO;
import java.util.List;
import java.util.Optional;

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

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
  
    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String getBio(long id) {
        Optional<User> book = userRepository.findById(id);
        return book.map(User::getBio).orElse(null);
    }

    public String getUserName(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getName();
        }
        return "Unknown User";
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User UpdateUser(Long userId, String name, String email,String password, String bio, HttpSession session) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if(name != null){
            user.setName(name);
        }
        if(email != null) {
            user.setEmail(email);
        }
        if(password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(bio != null){
            user.setBio(bio);
        }
        return userRepository.save(user);
    }

    public boolean updateBio(Long userId, String bio) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setBio(bio);
        return userRepository.save(user) != null;
    }
}
