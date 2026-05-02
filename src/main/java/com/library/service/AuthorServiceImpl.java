package com.library.service;

import com.library.entity.Author;
import com.library.exception.DuplicateEmailException;
import com.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public Author save(Author author) {
        // Guard against duplicate email on create
        authorRepository.findByEmail(author.getEmail()).ifPresent(existing -> {
            throw new DuplicateEmailException(author.getEmail());
        });
        return authorRepository.save(author);
    }

    @Override
    public Author update(Author author) {
        // Guard against stealing another author's email on update
        if (authorRepository.existsByEmailAndIdNot(author.getEmail(), author.getId())) {
            throw new DuplicateEmailException(author.getEmail());
        }
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
