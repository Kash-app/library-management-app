package com.library.controller;

import com.library.entity.Book;
import com.library.exception.DuplicateIsbnException;
import com.library.service.AuthorService;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    // -------------------------------------------------------------------------
    // READ — list all books with joined author details
    // -------------------------------------------------------------------------
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("bookViews", bookService.findAllBooksWithAuthorDetails());
        return "book-list";
    }

    // -------------------------------------------------------------------------
    // CREATE — show form
    // -------------------------------------------------------------------------
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("formTitle", "Add New Book");
        return "book-form";
    }

    // CREATE — handle submission
    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("formTitle", "Add New Book");
            return "book-form";
        }
        try {
            bookService.save(book);
            redirectAttrs.addFlashAttribute("successMessage", "Book added successfully.");
            return "redirect:/books";
        } catch (DuplicateIsbnException ex) {
            bindingResult.rejectValue("isbn", "duplicate.isbn", ex.getMessage());
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("formTitle", "Add New Book");
            return "book-form";
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE — show pre-populated form
    // -------------------------------------------------------------------------
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        return bookService.findById(id).map(book -> {
            model.addAttribute("book", book);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("formTitle", "Edit Book");
            return "book-form";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("errorMessage", "Book not found.");
            return "redirect:/books";
        });
    }

    // UPDATE — handle submission
    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttrs) {
        book.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("formTitle", "Edit Book");
            return "book-form";
        }
        try {
            bookService.update(book);
            redirectAttrs.addFlashAttribute("successMessage", "Book updated successfully.");
            return "redirect:/books";
        } catch (DuplicateIsbnException ex) {
            bindingResult.rejectValue("isbn", "duplicate.isbn", ex.getMessage());
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("formTitle", "Edit Book");
            return "book-form";
        }
    }
}
