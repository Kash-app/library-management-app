package com.library.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("An author with email '" + email + "' already exists.");
    }
}
