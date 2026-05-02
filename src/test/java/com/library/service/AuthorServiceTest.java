package com.library.service;

import com.library.entity.Author;
import com.library.exception.DuplicateEmailException;
import com.library.repository.AuthorRepository;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author tolkien;

    @BeforeEach
    void setUp() {
        tolkien = new Author("J.R.R. Tolkien", "tolkien@example.com", "British", 1892);
        tolkien.setId(1L);
    }

    // ── findAll ──────────────────────────────────────────────────────────────

    @Test
    void findAll_shouldDelegateToRepository() {
        when(authorRepository.findAll()).thenReturn(List.of(tolkien));
        List<Author> result = authorService.findAll();
        assertThat(result).containsExactly(tolkien);
        verify(authorRepository).findAll();
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test
    void findById_shouldReturnAuthorWhenPresent() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(tolkien));
        assertThat(authorService.findById(1L)).contains(tolkien);
    }

    @Test
    void findById_shouldReturnEmptyWhenAbsent() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThat(authorService.findById(99L)).isEmpty();
    }

    // ── save ─────────────────────────────────────────────────────────────────

    @Test
    void save_shouldPersistAuthorWhenEmailIsUnique() {
        when(authorRepository.findByEmail("tolkien@example.com")).thenReturn(Optional.empty());
        when(authorRepository.save(tolkien)).thenReturn(tolkien);

        Author saved = authorService.save(tolkien);
        assertThat(saved).isEqualTo(tolkien);
        verify(authorRepository).save(tolkien);
    }

    @Test
    void save_shouldThrowDuplicateEmailExceptionWhenEmailTaken() {
        Author existing = new Author("Other", "tolkien@example.com", "American", 1950);
        existing.setId(2L);
        when(authorRepository.findByEmail("tolkien@example.com")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> authorService.save(tolkien))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("tolkien@example.com");

        verify(authorRepository, never()).save(any());
    }

    // ── update ────────────────────────────────────────────────────────────────

    @Test
    void update_shouldSaveWhenNoEmailConflict() {
        when(authorRepository.existsByEmailAndIdNot("tolkien@example.com", 1L)).thenReturn(false);
        when(authorRepository.save(tolkien)).thenReturn(tolkien);

        Author updated = authorService.update(tolkien);
        assertThat(updated).isEqualTo(tolkien);
    }

    @Test
    void update_shouldThrowWhenEmailConflictsWithAnotherAuthor() {
        when(authorRepository.existsByEmailAndIdNot("tolkien@example.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> authorService.update(tolkien))
                .isInstanceOf(DuplicateEmailException.class);

        verify(authorRepository, never()).save(any());
    }
}
