package com.library.service;

import com.library.entity.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> findAll();

    Optional<Author> findById(Long id);

    /**
     * Saves a new author. Throws DuplicateEmailException if the email is already
     * registered to another author.
     */
    Author save(Author author);

    /**
     * Updates an existing author. Throws DuplicateEmailException if the updated
     * email conflicts with a different existing author.
     */
    Author update(Author author);

    void deleteById(Long id);
}
