package com.library.service;

import com.library.dto.BookAuthorView;
import com.library.entity.Book;
import com.library.exception.DuplicateIsbnException;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        bookRepository.findByIsbn(book.getIsbn()).ifPresent(existing -> {
            throw new DuplicateIsbnException(book.getIsbn());
        });
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        if (bookRepository.existsByIsbnAndIdNot(book.getIsbn(), book.getId())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookAuthorView> findAllBooksWithAuthorDetails() {
        return bookRepository.findAllBooksWithAuthorDetails();
    }
}
