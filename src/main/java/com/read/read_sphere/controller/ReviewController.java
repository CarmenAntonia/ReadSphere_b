package com.read.read_sphere.controller;

import com.read.read_sphere.DTOs.ReviewDTO;
import com.read.read_sphere.services.ReviewServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewServices reviewServices;

    public ReviewController(ReviewServices reviewServices) {
        this.reviewServices = reviewServices;
    }

    @GetMapping("/review/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForBook(@PathVariable("bookId") Long bookId) {
        var reviews = reviewServices.getReviewsForBooks(bookId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/review")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        if(reviewServices.createReview(reviewDTO) != null)
            return ResponseEntity.ok(reviewDTO);
        return ResponseEntity.badRequest().build();
    }
}
