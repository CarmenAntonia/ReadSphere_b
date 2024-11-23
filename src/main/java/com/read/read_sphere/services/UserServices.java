package com.read.read_sphere.services;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.read.read_sphere.DTOs.LoginUserDTO;

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
}
