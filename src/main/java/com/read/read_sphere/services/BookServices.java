package com.read.read_sphere.services;

import com.read.read_sphere.DTOs.BookDTO;
import com.read.read_sphere.model.Book;
import com.read.read_sphere.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServices {
    private final BookRepository bookRepository;

    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void CreateBooks(List<BookDTO> books) {
        for (BookDTO book : books) {
            BookDTO.VolumeInfo volumeInfo = book.getVolumeInfo();
            if (volumeInfo != null &&
                    volumeInfo.getAuthors() != null && !volumeInfo.getAuthors().isEmpty() &&
                    volumeInfo.getTitle() != null && !volumeInfo.getTitle().isEmpty() &&
                    volumeInfo.getDescription() != null && !volumeInfo.getDescription().isEmpty() ){

                Book bookEntity = new Book();
                bookEntity.setTitle(volumeInfo.getTitle());
                bookEntity.setAuthor(volumeInfo.getAuthors().get(0));
                bookEntity.setPublisher(volumeInfo.getPublisher());
                bookEntity.setDescription(volumeInfo.getDescription());
                bookEntity.setPublicationDate(volumeInfo.getPublishedDate());
                bookEntity.setCoverPictureUrl(volumeInfo.getImageLinks() != null ? volumeInfo.getImageLinks().getThumbnail() : null);

                bookRepository.save(bookEntity);
            }
        }
    }

    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

}
