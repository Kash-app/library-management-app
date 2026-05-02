package com.library.repository;

import com.library.entity.Book;
import com.library.dto.BookAuthorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Check whether a given ISBN is already taken (excluding the book with the given id,
     * used during update validation).
     */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * Find all books belonging to a specific author.
     */
    List<Book> findByAuthorId(Long authorId);

    /**
     * Custom INNER JOIN query: returns a flat view of book details together with
     * the author's name and nationality. This join is performed in JPQL so it
     * works database-agnostically and avoids N+1 selects.
     */
    @Query("SELECT b.id AS bookId, b.title AS title, b.genre AS genre, " +
           "b.publishedYear AS publishedYear, b.isbn AS isbn, " +
           "a.id AS authorId, a.name AS authorName, a.nationality AS authorNationality " +
           "FROM Book b INNER JOIN b.author a")
    List<BookAuthorView> findAllBooksWithAuthorDetails();

    /**
     * Find a book by ISBN.
     */
    Optional<Book> findByIsbn(String isbn);
}
