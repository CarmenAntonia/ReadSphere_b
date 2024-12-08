package com.read.read_sphere.controller;

import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.services.UserBookshelfService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/bookshelves")
public class BookshelfController {
    @Autowired
    private UserBookshelfService userBookshelfService;

    @GetMapping
    public List<UserBookshelf> getBookshelves(@PathVariable Long userId) {
        return userBookshelfService.getUserBookshelf(userId);
    }

    @PostMapping
    public UserBookshelf addBookshelf(@PathVariable Long userId, @RequestBody UserBookshelf bookshelf) {
        return userBookshelfService.addUserBookshelf(userId, bookshelf);
    }

    @DeleteMapping("/{shelfId}")
    public void deleteBookshelf(@PathVariable Long userId, @PathVariable Long shelfId) {
        userBookshelfService.deleteUserBookshelf(userId, shelfId);
    }
}
