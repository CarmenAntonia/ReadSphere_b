package com.read.read_sphere.DTOs;

public class BookBookshelfDTO {
    private Long shelfId;
    private Long bookId;
    private String title;
    private String author;
    private String coverPictureUrl;
    private String shelfType;

    public BookBookshelfDTO(Long shelfId, Long bookId, String title, String author, String coverPictureUrl, String name) {
        this.shelfId = shelfId;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.coverPictureUrl = coverPictureUrl;
        this.shelfType = name;
    }

    public Long getShelfId() { return shelfId; }
    public Long getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCoverPictureUrl() { return coverPictureUrl; }
    public String getShelfType() { return shelfType; }
}
