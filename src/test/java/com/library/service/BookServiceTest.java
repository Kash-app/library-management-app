package com.library.service;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.exception.DuplicateIsbnException;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author tolkien;
    private Book fellowship;

    @BeforeEach
    void setUp() {
        tolkien = new Author("J.R.R. Tolkien", "tolkien@example.com", "British", 1892);
        tolkien.setId(1L);
        fellowship = new Book("The Fellowship of the Ring", "Fantasy", 1954, "978-0-618-57494-1", tolkien);
        fellowship.setId(10L);
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    void findAll_shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(fellowship));
        assertThat(bookService.findAll()).containsExactly(fellowship);
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    void findById_shouldReturnBookWhenPresent() {
        when(bookRepository.findById(10L)).thenReturn(Optional.of(fellowship));
        assertThat(bookService.findById(10L)).contains(fellowship);
    }

    @Test
    void findById_shouldReturnEmptyWhenAbsent() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThat(bookService.findById(99L)).isEmpty();
    }

    // ── save ──────────────────────────────────────────────────────────────────

    @Test
    void save_shouldPersistBookWhenIsbnIsUnique() {
        when(bookRepository.findByIsbn("978-0-618-57494-1")).thenReturn(Optional.empty());
        when(bookRepository.save(fellowship)).thenReturn(fellowship);

        Book saved = bookService.save(fellowship);
        assertThat(saved).isEqualTo(fellowship);
        verify(bookRepository).save(fellowship);
    }

    @Test
    void save_shouldThrowDuplicateIsbnExceptionWhenIsbnTaken() {
        Book other = new Book("Other Book", "Genre", 2000, "978-0-618-57494-1", tolkien);
        other.setId(20L);
        when(bookRepository.findByIsbn("978-0-618-57494-1")).thenReturn(Optional.of(other));

        assertThatThrownBy(() -> bookService.save(fellowship))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("978-0-618-57494-1");

        verify(bookRepository, never()).save(any());
    }

    // ── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldSaveWhenNoIsbnConflict() {
        when(bookRepository.existsByIsbnAndIdNot("978-0-618-57494-1", 10L)).thenReturn(false);
        when(bookRepository.save(fellowship)).thenReturn(fellowship);

        Book updated = bookService.update(fellowship);
        assertThat(updated).isEqualTo(fellowship);
    }

    @Test
    void update_shouldThrowWhenIsbnConflictsWithAnotherBook() {
        when(bookRepository.existsByIsbnAndIdNot("978-0-618-57494-1", 10L)).thenReturn(true);

        assertThatThrownBy(() -> bookService.update(fellowship))
                .isInstanceOf(DuplicateIsbnException.class);

        verify(bookRepository, never()).save(any());
    }
}
