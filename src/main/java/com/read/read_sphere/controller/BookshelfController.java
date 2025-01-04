package com.read.read_sphere.controller;

import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.services.UserBookshelfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/bookshelves")
public class BookshelfController {

    @Autowired
    private UserBookshelfService userBookshelfService;

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserBookshelf>> getUserBookshelves(@PathVariable Long userId) {
        List<UserBookshelf> bookshelves = userBookshelfService.getShelvesByUserId(userId);
        return ResponseEntity.ok(bookshelves);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{userId}")
    public UserBookshelf createOrGetShelf(@PathVariable Long userId, @RequestParam String shelfName) {
        return userBookshelfService.getOrCreateShelf(userId, shelfName);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{userId}/{shelfName}/add-book/{bookId}")
    public ResponseEntity<UserBookshelf> addBookToShelf(@PathVariable Long userId, @PathVariable String shelfName, @PathVariable Long bookId) {
        UserBookshelf updatedShelf = userBookshelfService.addBookToShelf(userId, shelfName, bookId);
        return ResponseEntity.ok(updatedShelf);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @DeleteMapping("/{userId}/{shelfName}/remove-book/{bookId}")
    public ResponseEntity<Void> removeBookFromShelf(@PathVariable Long userId, @PathVariable String shelfName, @PathVariable Long bookId) {
        userBookshelfService.removeBookFromShelf(userId, shelfName, bookId);
        return ResponseEntity.noContent().build();
    }
}