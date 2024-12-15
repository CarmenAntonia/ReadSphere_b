package com.read.read_sphere.repository;

import org.springframework.stereotype.Repository;
import com.read.read_sphere.model.UserBookshelf;

@Repository
public interface UserBookshelfRepository extends CrudRepository<UserBookshelf, Long> {

}