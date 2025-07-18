package com.ra.ss4.controller;

import com.ra.ss4.entity.Book;
import com.ra.ss4.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false) String title,
                            Model model) {
        Page<Book> bookPage;

        if (title != null && !title.trim().isEmpty()) {
            bookPage = bookService.searchBookByTitle(title, page, size);
            model.addAttribute("title", title);
        } else {
            bookPage = bookService.findAllBooks(page, size);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalElements", bookPage.getTotalElements());
        model.addAttribute("size", size);

        return "books/list";
    }

    @GetMapping("/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("action", "add");
        return "books/form";
    }

    @PostMapping("/new")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");
            return "books/form";
        }

        try {
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Thêm sách thành công: " + book.getTitle());
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("action", "add");
            return "books/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") long id, Model model) {
        Optional<Book> book = bookService.findBookById(id);

        if (book.isEmpty()) {
            model.addAttribute("errorMessage", "Không tìm thấy sách với ID: " + id);
            return "redirect:/books";
        }

        model.addAttribute("book", book.get());
        model.addAttribute("action", "edit");
        return "books/form";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") long id, @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "books/form";
        }

        try {
            bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sách thành công: " + book.getTitle());
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("action", "edit");
            return "books/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            Optional<Book> book = bookService.findBookById(id);
            if (book.isPresent()) {
                bookService.deleteBook(id);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Xóa sách thành công: " + book.get().getTitle());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Không tìm thấy sách với ID: " + id);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi xóa sách: " + e.getMessage());
        }

        return "redirect:/books";
    }

    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookService.findBookById(id);

        if (book.isEmpty()) {
            model.addAttribute("errorMessage", "Không tìm thấy sách với ID: " + id);
            return "redirect:/books";
        }

        model.addAttribute("book", book.get());
        return "books/view";
    }

    // Tìm kiếm nâng cao
    @GetMapping("/search")
    public String showAdvancedSearchForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/advanced-search";
    }

    // Xử lý tìm kiếm nâng cao
    @PostMapping("/search")
    public String advancedSearch(@RequestParam(required = false) String title,
                                 @RequestParam(required = false) String author,
                                 @RequestParam(required = false) String publisher,
                                 @RequestParam(required = false) Integer year,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {

        Page<Book> bookPage = bookService.searchBooks(title, author, publisher, year, page, size);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalElements", bookPage.getTotalElements());
        model.addAttribute("size", size);

        // Giữ lại các tham số tìm kiếm
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchAuthor", author);
        model.addAttribute("searchPublisher", publisher);
        model.addAttribute("searchYear", year);

        return "books/search-results";
    }
}