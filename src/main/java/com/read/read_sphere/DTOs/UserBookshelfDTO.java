package com.read.read_sphere.DTOs;

import com.read.read_sphere.model.UserBookshelf;

public class UserBookshelfDTO {
    Long userId;
    Long bookId;
    UserBookshelf.ShelfType shelfType;

    public UserBookshelfDTO(Long userId, Long bookId, UserBookshelf.ShelfType shelfType) {
        this.userId = userId;
        this.bookId = bookId;
        this.shelfType = shelfType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public UserBookshelf.ShelfType getShelfType() {
        return shelfType;
    }
}
