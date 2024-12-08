package com.read.read_sphere.services;

import com.read.read_sphere.model.User;
import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.repository.UserBookshelfRepository;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookshelfService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookshelfRepository userBookshelfRepository;

    public List<UserBookshelf> getUserBookshelf(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBookshelves();
    }
    public UserBookshelf addUserBookshelf(Long userId, UserBookshelf bookshelf) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        bookshelf.setUser(user);
        userBookshelfRepository.save(bookshelf);
        return bookshelf;
    }

    public void deleteUserBookshelf(Long userId, Long userBookshelfId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userBookshelfRepository.deleteById(userBookshelfId);
        userRepository.delete(user);
    }
}
