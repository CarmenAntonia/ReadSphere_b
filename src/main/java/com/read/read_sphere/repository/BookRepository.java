package com.read.read_sphere.repository;

import org.springframework.stereotype.Repository;
import com.read.read_sphere.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

}