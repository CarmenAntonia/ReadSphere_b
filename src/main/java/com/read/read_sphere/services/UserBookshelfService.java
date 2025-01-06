package com.read.read_sphere.services;

import com.read.read_sphere.model.Book;
import com.read.read_sphere.model.User;
import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.repository.BookRepository;
import com.read.read_sphere.repository.UserBookshelfRepository;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserBookshelfService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookshelfRepository userBookshelfRepository;

    @Autowired
    private BookRepository bookRepository;

    public Map<String, List<Book>> getShelvesWithBooksByUserId(Long userId) {
        List<UserBookshelf> shelves = userBookshelfRepository.findByUserIdWithBooks(userId);
        Map<String, List<Book>> shelvesWithBooks = new HashMap<>();
        for (UserBookshelf shelf : shelves) {
            shelvesWithBooks.put(shelf.getShelfName(), shelf.getBooks());
        }
        return shelvesWithBooks;
    }


    public UserBookshelf addBookToShelf(Long userId, String shelfName, Long bookId) {
        Optional<UserBookshelf> shelfOpt = userBookshelfRepository.findByUserUserIdAndShelfName(userId, shelfName);

        if (shelfOpt.isPresent()) {
            UserBookshelf shelf = shelfOpt.get();

            Optional<Book> bookOpt = bookRepository.findByBookId(bookId);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();

                shelf.getBooks().add(book);

                userBookshelfRepository.save(shelf);
                return shelf;
            } else {
                throw new RuntimeException("Book not found!");
            }
        } else {
            throw new RuntimeException("Shelf not found!");
        }
    }

    public void removeBookFromShelf( Long userId, String shelfName, Long bookId) {
        Optional<UserBookshelf> shelfOpt = userBookshelfRepository.findByUserUserIdAndShelfName(userId, shelfName);

        if (shelfOpt.isPresent()) {
            UserBookshelf shelf = shelfOpt.get();

            Optional<Book> bookOpt = bookRepository.findByBookId(bookId);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();

                if (shelf.getBooks().contains(book)) {
                    shelf.getBooks().remove(book);
                    userBookshelfRepository.save(shelf);
                } else {
                    throw new RuntimeException("Book not found on the shelf!");
                }
            } else {
                throw new RuntimeException("Book not found!");
            }
        } else {
            throw new RuntimeException("Shelf not found!");
        }
    }
    public void createDefaultShelves(Long userId) {
        List<String> defaultShelves = List.of("myReadings", "myFutureReadings", "myCurrentReadings");

        for (String shelfName : defaultShelves) {
            userBookshelfRepository.findByUserUserIdAndShelfName(userId, shelfName).orElseGet(() -> {
                UserBookshelf newShelf = new UserBookshelf();
                User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
                newShelf.setUser(user);
                newShelf.setShelfName(shelfName);
                return userBookshelfRepository.save(newShelf);
            });
        }
    }

}
