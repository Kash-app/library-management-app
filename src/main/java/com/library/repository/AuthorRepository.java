package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find an author by their unique email address.
     */
    Optional<Author> findByEmail(String email);

    /**
     * Find all authors matching the given nationality, case-insensitive.
     */
    @Query("SELECT a FROM Author a WHERE LOWER(a.nationality) = LOWER(:nationality)")
    List<Author> findByNationalityIgnoreCase(String nationality);

    /**
     * Check whether an email is already in use (excluding the author with the given id,
     * used during update validation).
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}
