package com.fs.main.service;

import com.fs.main.dto.CategoryDto;
import com.fs.main.entity.Category;
import com.fs.main.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name must not be empty");
        }

        try {
            Category category = new Category(dto.getName(), dto.getDescription());
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category", e);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return category;
    }

    public Category updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category = categoryRepository.save(category);
        return category;
    }

    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        try {
            categoryRepository.deleteById(id);
            return true;
        }catch (Exception cause){
            return false;
        }
    }
}

