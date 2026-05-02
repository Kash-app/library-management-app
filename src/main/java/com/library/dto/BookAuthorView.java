package com.library.dto;

/**
 * Projection interface used by the custom INNER JOIN query in BookRepository.
 * Spring Data JPA maps each column alias from the JPQL query to the matching
 * getter here at runtime, so no concrete implementation is required.
 */
public interface BookAuthorView {
    Long getBookId();
    String getTitle();
    String getGenre();
    Integer getPublishedYear();
    String getIsbn();
    Long getAuthorId();
    String getAuthorName();
    String getAuthorNationality();
}
