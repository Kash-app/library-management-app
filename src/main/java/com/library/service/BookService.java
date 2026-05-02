package com.library.service;

import com.library.dto.BookAuthorView;
import com.library.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    /**
     * Saves a new book. Throws DuplicateIsbnException if the ISBN is already taken.
     */
    Book save(Book book);

    /**
     * Updates an existing book. Throws DuplicateIsbnException if the updated
     * ISBN conflicts with a different existing book.
     */
    Book update(Book book);

    void deleteById(Long id);

    /**
     * Returns the result of the custom INNER JOIN query combining book and author data.
     */
    List<BookAuthorView> findAllBooksWithAuthorDetails();
}
