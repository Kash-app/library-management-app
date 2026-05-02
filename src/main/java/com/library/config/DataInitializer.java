package com.library.config;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds the database with 10 authors and 10 books on first startup.
 * The check against repository count prevents duplicate inserts on restart
 * when using a persistent data source.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDatabase(AuthorRepository authorRepo, BookRepository bookRepo) {
        return args -> {
            if (authorRepo.count() > 0) {
                return; // already seeded
            }

            Author tolkien  = authorRepo.save(new Author("J.R.R. Tolkien",   "tolkien@oxford.ac.uk",  "British",    1892));
            Author orwell   = authorRepo.save(new Author("George Orwell",     "orwell@secker.co.uk",   "British",    1903));
            Author austen   = authorRepo.save(new Author("Jane Austen",       "austen@steventon.uk",   "British",    1775));
            Author kafka    = authorRepo.save(new Author("Franz Kafka",       "kafka@prager-str.cz",   "Czech",      1883));
            Author dostoev  = authorRepo.save(new Author("Fyodor Dostoevsky","dostoevsky@spb.ru",      "Russian",    1821));
            Author hemingw  = authorRepo.save(new Author("Ernest Hemingway", "hemingway@paris.fr",     "American",   1899));
            Author woolf    = authorRepo.save(new Author("Virginia Woolf",    "woolf@bloomsbury.co.uk","British",    1882));
            Author garcia   = authorRepo.save(new Author("Gabriel García Márquez","garcia@cartagena.co","Colombian", 1927));
            Author camus    = authorRepo.save(new Author("Albert Camus",      "camus@algiers.dz",       "French",     1913));
            Author austen2  = authorRepo.save(new Author("Haruki Murakami",   "murakami@tokyo.jp",      "Japanese",   1949));

            bookRepo.save(new Book("The Fellowship of the Ring", "Fantasy",   1954, "978-0-618-57494-1", tolkien));
            bookRepo.save(new Book("Nineteen Eighty-Four",        "Dystopian", 1949, "978-0-452-28423-4", orwell));
            bookRepo.save(new Book("Pride and Prejudice",          "Romance",   1813, "978-0-14-143951-8", austen));
            bookRepo.save(new Book("The Metamorphosis",            "Absurdist", 1915, "978-0-14-018966-0", kafka));
            bookRepo.save(new Book("Crime and Punishment",         "Thriller",  1866, "978-0-14-044913-6", dostoev));
            bookRepo.save(new Book("The Old Man and the Sea",      "Literary",  1952, "978-0-684-80122-3", hemingw));
            bookRepo.save(new Book("Mrs Dalloway",                 "Modernist", 1925, "978-0-15-662870-9", woolf));
            bookRepo.save(new Book("One Hundred Years of Solitude","Magical Realism", 1967, "978-0-06-088328-7", garcia));
            bookRepo.save(new Book("The Stranger",                 "Absurdist", 1942, "978-0-67-973020-1", camus));
            bookRepo.save(new Book("Norwegian Wood",               "Literary",  1987, "978-0-37-571029-5", austen2));
        };
    }
}
