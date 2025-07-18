package com.ra.ss4.service;

import com.ra.ss4.entity.Category;
import com.ra.ss4.entity.FoodItem;
import com.ra.ss4.repository.CategoryRepository;
import com.ra.ss4.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả thực phẩm với phân trang
    public Page<FoodItem> getAllFoodItems(Pageable pageable) {
        return foodItemRepository.findAll(pageable);
    }

    // Lấy thực phẩm theo ID
    public Optional<FoodItem> getFoodItemById(Long id) {
        return foodItemRepository.findById(id);
    }

    // Lưu thực phẩm mới
    public FoodItem saveFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // Cập nhật thực phẩm
    public FoodItem updateFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // Xóa thực phẩm
    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
    }

    // Tìm kiếm và lọc thực phẩm
    public Page<FoodItem> searchFoodItems(String name, Long categoryId, Pageable pageable) {
        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId).orElse(null);
        }

        if (name != null && !name.trim().isEmpty() && category != null) {
            return foodItemRepository.findByNameContainingIgnoreCaseAndCategory(name, category, pageable);
        } else if (name != null && !name.trim().isEmpty()) {
            return foodItemRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null) {
            return foodItemRepository.findByCategory(category, pageable);
        } else {
            return foodItemRepository.findAll(pageable);
        }
    }

    // Lấy tất cả categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Lấy category theo ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
}