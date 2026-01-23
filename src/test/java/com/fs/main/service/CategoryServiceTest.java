package com.fs.main.service;

import com.fs.main.dto.CategoryDto;
import com.fs.main.entity.Category;
import com.fs.main.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    // Create category
    @Test
    void shouldCreateCategorySuccessfully() {

        CategoryDto dto = new CategoryDto("Chair", "Wooden chair");

        Category saved = new Category();
        saved.setCategoryId(1L);
        saved.setName("Chair");
        saved.setDescription("Wooden chair");

        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenReturn(saved);

        Category result = categoryService.createCategory(dto);

        assertNotNull(result);
        assertEquals("Chair", result.getName());
        assertEquals("Wooden chair", result.getDescription());
    }

    // should throw exception when category name is null
    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        CategoryDto dto = new CategoryDto(null, "Desc");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.createCategory(dto)
        );

        assertEquals("Category name must not be empty", ex.getMessage());
    }

    // should throw exception when category save fail
    @Test
    void shouldThrowExceptionWhenRepositoryFails() {

        CategoryDto dto = new CategoryDto("Table", "Desc");

        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.createCategory(dto)
        );

        assertEquals("Failed to create category", ex.getMessage());
    }

    // get all categories
    @Test
    void shouldReturnAllCategories() {

        Category c1 = new Category(1L, "Chair", "Wooden");
        Category c2 = new Category(2L, "Table", "Dining");

        List<Category> mockList = List.of(c1, c2);

        Mockito.when(categoryRepository.findAll())
                .thenReturn(mockList);

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(mockList.size(), result.size());
    }

    // Return empty list
    @Test
    void shouldReturnEmptyListWhenNoCategoriesFound() {

        Mockito.when(categoryRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        Mockito.verify(categoryRepository).findAll();
    }

    // return category by Id
    @Test
    void shouldReturnCategoryById() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName("Chair");
        category.setDescription("Wooden");

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("Chair", result.getName());
        assertEquals("Wooden", result.getDescription());
    }

    // Throw exception when Id not found
    @Test
    void shouldFailedWhenCategoryNotFoundById() {

        Long categoryId = 99L;

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.getCategoryById(categoryId)
        );

        assertEquals("Category not found", ex.getMessage());
    }

    // Should update category successfully
    @Test
    void shouldUpdateCategorySuccessfully() {

        Long id = 1L;

        Category existing = new Category();
        existing.setCategoryId(id);
        existing.setName("Old Name");
        existing.setDescription("Old Desc");

        CategoryDto dto = new CategoryDto("New Name", "New Desc");

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existing));

        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Category updated = categoryService.updateCategory(id, dto);

        assertNotNull(updated);
        assertEquals(dto.getName(), updated.getName());
        assertEquals(dto.getDescription(), updated.getDescription());
    }

    // failed update category
    @Test
    void shouldFailedCategoryUpdate() {

        Long id = 99L;
        CategoryDto dto = new CategoryDto("Name", "Desc");

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(id, dto)
        );

        assertEquals("Category not found", ex.getMessage());
    }

    // Should delete by id
    @Test
    void shouldDeleteCategory() {

        Long categoryId = 1L;

        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(true);

        boolean result = categoryService.deleteCategory(categoryId);

        Assertions.assertTrue(result);
    }

    // Should failed when categoryId not found
    @Test
    void shouldFailedWhenCategoryNotDelete() {

        Long categoryId = 1L;

        Mockito.when(categoryRepository.existsById(categoryId))
                .thenReturn(false);

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> categoryService.deleteCategory(categoryId)
        );
        Assertions.assertEquals("Category not found", exception.getMessage());
    }
}
