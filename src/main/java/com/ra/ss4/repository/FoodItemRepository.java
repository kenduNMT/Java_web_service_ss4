package com.ra.ss4.repository;

import com.ra.ss4.entity.FoodItem;
import com.ra.ss4.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // Tìm kiếm theo tên
    Page<FoodItem> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Lọc theo category
    Page<FoodItem> findByCategory(Category category, Pageable pageable);

    // Tìm kiếm theo tên và lọc theo category
    Page<FoodItem> findByNameContainingIgnoreCaseAndCategory(String name, Category category, Pageable pageable);

    // Tìm kiếm theo tên hoặc category
    @Query("SELECT f FROM FoodItem f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR f.category = :category)")
    Page<FoodItem> findByNameAndCategory(@Param("name") String name,
                                         @Param("category") Category category,
                                         Pageable pageable);
}
