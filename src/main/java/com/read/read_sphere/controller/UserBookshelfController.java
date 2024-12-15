package com.read.read_sphere.controller;

import com.read.read_sphere.model.Book;
import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.services.UserBookshelfService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/bookshelf")
public class UserBookshelfController {

    private final UserBookshelfService userBookshelfService;

    public UserBookshelfController(UserBookshelfService userBookshelfService) {
        this.userBookshelfService = userBookshelfService;
    }

}
