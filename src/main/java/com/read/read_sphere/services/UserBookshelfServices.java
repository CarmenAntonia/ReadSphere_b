package com.read.read_sphere.services;

import com.read.read_sphere.DTOs.UserBookshelfDTO;
import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.repository.BookRepository;
import com.read.read_sphere.repository.UserBookshelfRepository;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookshelfServices {
    private final UserBookshelfRepository userBookshelfRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    public UserBookshelfServices(UserBookshelfRepository userBookshelfRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.userBookshelfRepository = userBookshelfRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public UserBookshelf addBookToShelf(UserBookshelfDTO bookshelfDTO) {
        var user = userRepository.findById(bookshelfDTO.getUserId()).orElse(null);
        var book = bookRepository.findById(bookshelfDTO.getBookId()).orElse(null);
        if(user != null && book != null) {
            return userBookshelfRepository.save(new UserBookshelf(user, book, bookshelfDTO.getShelfType()));
        }
        return null;
    }

    public List<UserBookshelf> getUserBookshelf(Long userId) {
        return userBookshelfRepository.findAllByUserUserId(userId);
    }
}
