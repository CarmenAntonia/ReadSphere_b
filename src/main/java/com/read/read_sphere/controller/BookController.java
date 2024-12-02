package com.read.read_sphere.controller;

import com.read.read_sphere.DTOs.BookDTO;
import com.read.read_sphere.DTOs.GoogleBookResponse;
import com.read.read_sphere.model.Book;
import com.read.read_sphere.services.BookServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@RestController
public class BookController {
    private final BookServices bookServices;
    private final Logger logger = Logger.getLogger(BookController.class.getName());

    @Value("${google.apiKey}")
    private String apiKey;

    public BookController(BookServices bookServices) {
        this.bookServices = bookServices;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getBooksFromGoogle() {
        RestTemplate restTemplate = new RestTemplate();
        String[] keywords = {"a", "b", "c", "d", "e", "f"};
        String randomKeyword = keywords[new Random().nextInt(keywords.length)];

        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                .queryParam("q", randomKeyword)
                .queryParam("printType", "books")
                .queryParam("maxResults", 30)
                .queryParam("key", apiKey)
                .toUriString();

        ResponseEntity<GoogleBookResponse> response = restTemplate.getForEntity(url, GoogleBookResponse.class);

        GoogleBookResponse responseBody = response.getBody();
        if (responseBody != null && responseBody.getItems() != null) {
            bookServices.CreateBooks(responseBody.getItems());
            return ResponseEntity.ok(responseBody.getItems());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/getBooks")
    public ResponseEntity<Iterable<Book>> getBooks() {
        Iterable<Book> books;
        books = bookServices.getBooks();
        if (books != null) {
            return ResponseEntity.ok(books);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book")
    public ResponseEntity<Book> getBookById(@RequestParam("id") long id) {
        Book book = bookServices.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.noContent().build();
    }
}
