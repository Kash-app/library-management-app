package com.library.controller;

import com.library.entity.Author;
import com.library.exception.DuplicateEmailException;
import com.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // -------------------------------------------------------------------------
    // READ — list all authors
    // -------------------------------------------------------------------------
    @GetMapping
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "author-list";
    }

    // -------------------------------------------------------------------------
    // CREATE — show form
    // -------------------------------------------------------------------------
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("formTitle", "Add New Author");
        return "author-form";
    }

    // CREATE — handle submission
    @PostMapping
    public String createAuthor(@Valid @ModelAttribute("author") Author author,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Add New Author");
            return "author-form";
        }
        try {
            authorService.save(author);
            redirectAttrs.addFlashAttribute("successMessage", "Author added successfully.");
            return "redirect:/authors";
        } catch (DuplicateEmailException ex) {
            bindingResult.rejectValue("email", "duplicate.email", ex.getMessage());
            model.addAttribute("formTitle", "Add New Author");
            return "author-form";
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE — show pre-populated form
    // -------------------------------------------------------------------------
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        return authorService.findById(id).map(author -> {
            model.addAttribute("author", author);
            model.addAttribute("formTitle", "Edit Author");
            return "author-form";
        }).orElseGet(() -> {
            redirectAttrs.addFlashAttribute("errorMessage", "Author not found.");
            return "redirect:/authors";
        });
    }

    // UPDATE — handle submission
    @PostMapping("/{id}/edit")
    public String updateAuthor(@PathVariable Long id,
                               @Valid @ModelAttribute("author") Author author,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttrs) {
        author.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Edit Author");
            return "author-form";
        }
        try {
            authorService.update(author);
            redirectAttrs.addFlashAttribute("successMessage", "Author updated successfully.");
            return "redirect:/authors";
        } catch (DuplicateEmailException ex) {
            bindingResult.rejectValue("email", "duplicate.email", ex.getMessage());
            model.addAttribute("formTitle", "Edit Author");
            return "author-form";
        }
    }
}
