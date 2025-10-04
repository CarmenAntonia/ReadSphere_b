package com.read.read_sphere.controller;

import com.read.read_sphere.DTOs.BookBookshelfDTO;
import com.read.read_sphere.DTOs.UserBookshelfDTO;
import com.read.read_sphere.model.UserBookshelf;
import com.read.read_sphere.services.UserBookshelfServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserBookshelfController {
    private final UserBookshelfServices userBookshelfServices;

    public UserBookshelfController(UserBookshelfServices userBookshelfServices) {
        this.userBookshelfServices = userBookshelfServices;
    }

    @PostMapping("/bookshelf")
    public ResponseEntity<UserBookshelf> addBookToShelf(@RequestBody UserBookshelfDTO bookshelfDTO) {
        var bookShelf = userBookshelfServices.addBookToShelf(bookshelfDTO);
        if (bookShelf != null) {
            return ResponseEntity.ok(bookShelf);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bookshelf/{userId}")
    public ResponseEntity<List<BookBookshelfDTO>> getBookshelvesForUser(@PathVariable Long userId) {
        List<UserBookshelf> shelves = userBookshelfServices.getUserBookshelf(userId);
        if (shelves == null || shelves.isEmpty()) return ResponseEntity.notFound().build();

        List<BookBookshelfDTO> dto = shelves.stream().map(sb -> new BookBookshelfDTO(
                sb.getShelfId(),
                sb.getBook().getBookId(),
                sb.getBook().getTitle(),
                sb.getBook().getAuthor(),
                sb.getBook().getCoverPictureUrl(),
                sb.getShelfType().name()
        )).toList();

        return ResponseEntity.ok(dto);
    }
}
