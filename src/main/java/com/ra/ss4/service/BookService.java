package com.ra.ss4.service;

import com.ra.ss4.entity.Book;
import com.ra.ss4.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> findAllBooks(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }

    public Page<Book> findAllBooks(int page, int size) {
        return findAllBooks(page, size, "id", "desc");
    }

    public Page<Book> searchBookByTitle(String title, int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (title == null || title.trim().isEmpty()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void saveBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new RuntimeException("Book already exists with title: " + book.getTitle() + " and author: " + book.getAuthor());
        }

        bookRepository.save(book);
    }

    public void updateBook(Long id, Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found with id: " + id);
        }

        Book existingBook = optionalBook.get();

        if (!existingBook.getTitle().equals(bookDetails.getTitle()) || !existingBook.getAuthor().equals(bookDetails.getAuthor())) {
            if (bookRepository.existsByTitleAndAuthor(bookDetails.getTitle(), bookDetails.getAuthor())) {
                throw new RuntimeException("Book already exists with title: " + bookDetails.getTitle());
            }
        }

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setYear(bookDetails.getYear());
        bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public Page<Book> searchBooks(String title, String author,String publisher, Integer year, int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookRepository.findByMultipleCriteria(
                isEmptyOrNull(title) ? null : title,
                isEmptyOrNull(author) ? null : author,
                isEmptyOrNull(publisher) ? null : publisher,
                year,
                pageable
        );
    }

    // Kiểm tra chuỗi rỗng hoặc null
    private boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Kiểm tra sách có tồn tại
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
