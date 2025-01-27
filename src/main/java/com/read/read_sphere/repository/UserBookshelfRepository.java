package com.read.read_sphere.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.read.read_sphere.model.UserBookshelf;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookshelfRepository extends CrudRepository<UserBookshelf, Long> {
    Optional<UserBookshelf> findByUserUserIdAndShelfName(Long userId, String shelfName);

    @Query("SELECT DISTINCT ubs FROM UserBookshelf ubs " +
            "LEFT JOIN FETCH ubs.books " +
            "WHERE ubs.user.userId = :userId")
    List<UserBookshelf> findByUserIdWithBooks(@Param("userId") Long userId);

}