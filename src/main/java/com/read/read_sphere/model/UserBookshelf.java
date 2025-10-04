package com.read.read_sphere.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_bookshelves")
public class UserBookshelf {

    @Id
    @Column(name = "bookself_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shelfId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false, foreignKey = @ForeignKey(name = "fk_book_id"))
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "shelf_type", nullable = false, length = 30)
    private ShelfType shelfType;

    public UserBookshelf() {
    }

    public UserBookshelf(User user, Book book, ShelfType shelfType) {
        this.user = user;
        this.book = book;
        this.shelfType = shelfType;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public ShelfType getShelfType() { return shelfType; }
    public void setShelfType(ShelfType shelfType) { this.shelfType = shelfType; }

    public enum ShelfType {
        READ,
        CURRENTLY_READING,
        WANT_TO_READ
    }
}
