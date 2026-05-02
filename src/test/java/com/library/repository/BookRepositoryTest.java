package com.library.repository;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.dto.BookAuthorView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Author tolkien;
    private Book fellowship;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        tolkien = authorRepository.save(
                new Author("J.R.R. Tolkien", "tolkien@example.com", "British", 1892));
        Author orwell = authorRepository.save(
                new Author("George Orwell", "orwell@example.com", "British", 1903));

        fellowship = bookRepository.save(
                new Book("The Fellowship of the Ring", "Fantasy", 1954, "978-0-618-57494-1", tolkien));
        bookRepository.save(
                new Book("Nineteen Eighty-Four", "Dystopian", 1949, "978-0-452-28423-4", orwell));
    }

    @Test
    void findAll_shouldReturnBothBooks() {
        assertThat(bookRepository.findAll()).hasSize(2);
    }

    @Test
    void findByIsbn_shouldReturnCorrectBook() {
        Optional<Book> found = bookRepository.findByIsbn("978-0-618-57494-1");
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("The Fellowship of the Ring");
    }

    @Test
    void findByAuthorId_shouldOnlyReturnBooksForThatAuthor() {
        List<Book> tolkienBooks = bookRepository.findByAuthorId(tolkien.getId());
        assertThat(tolkienBooks).hasSize(1);
        assertThat(tolkienBooks.get(0).getTitle()).isEqualTo("The Fellowship of the Ring");
    }

    @Test
    void existsByIsbnAndIdNot_shouldDetectDuplicateIsbn() {
        boolean conflict = bookRepository.existsByIsbnAndIdNot(
                "978-0-618-57494-1",
                bookRepository.findByIsbn("978-0-452-28423-4").get().getId());
        assertThat(conflict).isTrue();
    }

    @Test
    void existsByIsbnAndIdNot_shouldNotFlagSameRecord() {
        boolean conflict = bookRepository.existsByIsbnAndIdNot(
                "978-0-618-57494-1", fellowship.getId());
        assertThat(conflict).isFalse();
    }

    @Test
    void findAllBooksWithAuthorDetails_shouldReturnJoinedRows() {
        List<BookAuthorView> views = bookRepository.findAllBooksWithAuthorDetails();
        assertThat(views).hasSize(2);

        BookAuthorView fellowshipView = views.stream()
                .filter(v -> "The Fellowship of the Ring".equals(v.getTitle()))
                .findFirst()
                .orElseThrow();

        assertThat(fellowshipView.getAuthorName()).isEqualTo("J.R.R. Tolkien");
        assertThat(fellowshipView.getAuthorNationality()).isEqualTo("British");
        assertThat(fellowshipView.getGenre()).isEqualTo("Fantasy");
    }
}
