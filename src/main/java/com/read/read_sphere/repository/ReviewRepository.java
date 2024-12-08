package com.read.read_sphere.repository;

import org.springframework.stereotype.Repository;
import com.read.read_sphere.model.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

}