package com.read.read_sphere.services;

import com.read.read_sphere.DTOs.ReviewDTO;
import com.read.read_sphere.model.Review;
import com.read.read_sphere.repository.BookRepository;
import com.read.read_sphere.repository.ReviewRepository;
import com.read.read_sphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServices {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewServices(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<ReviewDTO> getReviewsForBooks(Long bookId) {
        var reviews = reviewRepository.findByBookBookId(bookId).orElse(new ArrayList<Review>());
        return new ArrayList<>(reviews.stream().map(review ->
                new ReviewDTO(review.getUser().getUserId(),
                        review.getBook().getBookId(),
                        review.getRating(),
                        review.getReviewText(),
                        review.getUser().getName()
                )).toList()
        );
    }

    public Review createReview(ReviewDTO reviewDTO) {
        var user = userRepository.findById(reviewDTO.getUserId()).orElse(null);
        var book = bookRepository.findById(reviewDTO.getBookId()).orElse(null);
        if(user != null && book != null) {
            var review = new Review();
            review.setUser(user);
            review.setBook(book);
            review.setRating(reviewDTO.getRating());
            review.setReviewText(reviewDTO.getReviewText());
            return reviewRepository.save(review);
        }
        return null;
    }
}
