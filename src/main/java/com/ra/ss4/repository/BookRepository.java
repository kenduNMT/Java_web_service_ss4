package com.ra.ss4.repository;

import com.ra.ss4.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findByPublisherContainingIgnoreCase(String publisher, Pageable pageable);

    Page<Book> findByYear(Integer year, Pageable pageable);

    // Tìm kiếm sách theo nhiều tiêu chí
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:publisher IS NULL OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))) AND " +
            "(:year IS NULL OR b.year = :year)")
    Page<Book> findByMultipleCriteria(@Param("title") String title,
                                      @Param("author") String author,
                                      @Param("publisher") String publisher,
                                      @Param("year") Integer year,
                                      Pageable pageable);

    boolean existsByTitleAndAuthor(String title, String author);

    Page<Book> findByYearBetween(Integer startYear, Integer endYear, Pageable pageable);

    long countByAuthor(String author);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
