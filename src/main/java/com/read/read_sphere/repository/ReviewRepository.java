package com.read.read_sphere.repository;

import org.springframework.stereotype.Repository;
import com.read.read_sphere.model.Review;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    Optional<List<Review>> findByBookBookId(Long bookId);
}