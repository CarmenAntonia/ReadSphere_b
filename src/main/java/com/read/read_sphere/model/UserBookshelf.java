package com.read.read_sphere.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

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

    @ManyToMany
    @JoinTable(
            name = "books_bookshelves",
            joinColumns = @JoinColumn(name = "bookshelf_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();

    public UserBookshelf() {
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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
