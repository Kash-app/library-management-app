package com.library.repository;

import com.library.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Slice test: only the JPA layer is loaded (no web or service beans).
 * Spring Boot provides an in-memory H2 database automatically.
 */
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author tolkien;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        tolkien = authorRepository.save(
                new Author("J.R.R. Tolkien", "tolkien@example.com", "British", 1892));
        authorRepository.save(
                new Author("George Orwell", "orwell@example.com", "British", 1903));
        authorRepository.save(
                new Author("Franz Kafka", "kafka@example.com", "Czech", 1883));
    }

    @Test
    void findAll_shouldReturnAllSavedAuthors() {
        List<Author> all = authorRepository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void findByEmail_shouldReturnMatchingAuthor() {
        Optional<Author> found = authorRepository.findByEmail("tolkien@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("J.R.R. Tolkien");
    }

    @Test
    void findByEmail_shouldReturnEmptyForUnknownEmail() {
        Optional<Author> found = authorRepository.findByEmail("nobody@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    void findByNationalityIgnoreCase_shouldReturnBritishAuthors() {
        List<Author> british = authorRepository.findByNationalityIgnoreCase("british");
        assertThat(british).hasSize(2)
                           .extracting(Author::getName)
                           .containsExactlyInAnyOrder("J.R.R. Tolkien", "George Orwell");
    }

    @Test
    void existsByEmailAndIdNot_shouldReturnTrueWhenDuplicateExists() {
        // tolkien's email used with orwell's id => conflict exists
        boolean conflict = authorRepository.existsByEmailAndIdNot(
                "tolkien@example.com",
                authorRepository.findByEmail("orwell@example.com").get().getId());
        assertThat(conflict).isTrue();
    }

    @Test
    void existsByEmailAndIdNot_shouldReturnFalseForSameRecord() {
        // tolkien's email with tolkien's own id => no conflict
        boolean conflict = authorRepository.existsByEmailAndIdNot(
                "tolkien@example.com", tolkien.getId());
        assertThat(conflict).isFalse();
    }

    @Test
    void save_shouldPersistNewAuthor() {
        Author camus = authorRepository.save(
                new Author("Albert Camus", "camus@example.com", "French", 1913));
        assertThat(camus.getId()).isNotNull();
        assertThat(authorRepository.count()).isEqualTo(4);
    }

    @Test
    void findById_shouldReturnSavedAuthor() {
        Optional<Author> found = authorRepository.findById(tolkien.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getBirthYear()).isEqualTo(1892);
    }
}
