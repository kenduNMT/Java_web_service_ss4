package com.ra.ss4.controller;

import com.ra.ss4.entity.FoodItem;
import com.ra.ss4.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/food-items")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    // Hiển thị danh sách thực phẩm với phân trang và tìm kiếm
    @GetMapping
    public String listFoodItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            Model model) {

        Sort.Direction direction = sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<FoodItem> foodItemsPage = foodItemService.searchFoodItems(name, categoryId, pageable);

        model.addAttribute("foodItems", foodItemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", foodItemsPage.getTotalPages());
        model.addAttribute("totalItems", foodItemsPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", foodItemService.getAllCategories());

        return "food-items/list";
    }

    // Hiển thị form thêm thực phẩm mới
    @GetMapping("/new")
    public String showNewFoodItemForm(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("categories", foodItemService.getAllCategories());
        return "food-items/form";
    }

    // Xử lý thêm thực phẩm mới
    @PostMapping("/new")
    public String createFoodItem(@ModelAttribute FoodItem foodItem,
                                 RedirectAttributes redirectAttributes) {
        try {
            foodItemService.saveFoodItem(foodItem);
            redirectAttributes.addFlashAttribute("successMessage", "Thực phẩm đã được thêm thành công!");
            return "redirect:/food-items";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi thêm thực phẩm!");
            return "redirect:/food-items/new";
        }
    }

    // Hiển thị form cập nhật thực phẩm
    @GetMapping("/edit/{id}")
    public String showEditFoodItemForm(@PathVariable Long id, Model model) {
        Optional<FoodItem> foodItem = foodItemService.getFoodItemById(id);
        if (foodItem.isPresent()) {
            model.addAttribute("foodItem", foodItem.get());
            model.addAttribute("categories", foodItemService.getAllCategories());
            return "food-items/form";
        } else {
            return "redirect:/food-items";
        }
    }

    // Xử lý cập nhật thực phẩm
    @PostMapping("/edit/{id}")
    public String updateFoodItem(@PathVariable Long id,
                                 @ModelAttribute FoodItem foodItem,
                                 RedirectAttributes redirectAttributes) {
        try {
            foodItem.setId(id);
            foodItemService.updateFoodItem(foodItem);
            redirectAttributes.addFlashAttribute("successMessage", "Thực phẩm đã được cập nhật thành công!");
            return "redirect:/food-items";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thực phẩm!");
            return "redirect:/food-items/edit/" + id;
        }
    }

    // Xóa thực phẩm
    @PostMapping("/delete/{id}")
    public String deleteFoodItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            foodItemService.deleteFoodItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Thực phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa thực phẩm!");
        }
        return "redirect:/food-items";
    }

    // Trang chủ redirect
    @GetMapping("/")
    public String home() {
        return "redirect:/food-items";
    }
}