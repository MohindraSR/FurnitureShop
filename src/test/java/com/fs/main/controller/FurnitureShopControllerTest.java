package com.fs.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.main.dto.CategoryDto;
import com.fs.main.entity.Category;
import com.fs.main.service.CategoryService;
import com.fs.main.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(FurnitureShopController.class)
@Import({ControllerTestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class FurnitureShopControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ProductService productService;

    // Create Category
    @Test
    void shouldCreateCategoryAndRedirectToAllProducts() throws Exception {

        // repository return value
        Category saved = new Category();
        saved.setCategoryId(1L);
        saved.setName("Test_CategoryName");
        saved.setDescription("Test_CategoryDescription");

        Mockito.when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(saved);
        ArgumentCaptor<CategoryDto> dtoCaptor =
                ArgumentCaptor.forClass(CategoryDto.class);


        mockMvc.perform(post("/api/user/createCategory")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Test_CategoryName")
                        .param("description", "Test_CategoryDescription"))
                .andExpect(status().isOk())
                .andExpect(content().string("redirect:/api/user/allProducts"));
        Mockito.verify(categoryService).createCategory(dtoCaptor.capture());

        CategoryDto capturedDto = dtoCaptor.getValue();

        assertEquals(saved.getName(), capturedDto.getName());
        assertEquals(saved.getDescription(), capturedDto.getDescription());

    }


    // get all categories
    @Test
    void shouldReturnAllCategoriesAndViewName() throws Exception {

        Category c1 = new Category(1L, "Chair", "Wooden");
        Category c2 = new Category(2L, "Table", "Dining");

        List<Category> categoryList = List.of(c1, c2);

        Mockito.when(categoryService.getAllCategories())
                .thenReturn(categoryList);

        mockMvc.perform(get("/api/user/allCategories"))
                .andExpect(status().isOk())
                .andExpect(view().name("CustomerHomePage"))
                .andExpect(model().attribute("categoryList",
                        Matchers.hasSize(categoryList.size())))
                .andExpect(model().attribute("categoryList",
                Matchers.contains(
                        Matchers.hasProperty("name", Matchers.is(c1.getName())),
                        Matchers.hasProperty("description", Matchers.is(c2.getDescription()))
                )));
    }


    // Get category by id
    @Test
    void shouldReturnCategoryById() throws Exception {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName("Chair");
        category.setDescription("Wooden");

        Mockito.when(categoryService.getCategoryById(categoryId))
                .thenReturn(category);

        mockMvc.perform(get("/api/user/category/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("CustomerHomePage"))
                .andExpect(model().attribute("category",
                        Matchers.hasProperty("name", Matchers.is("Chair"))));
    }

    // Update category
    @Test
    void shouldUpdateCategory() throws Exception {

        Long categoryId = 1L;

        CategoryDto dto = new CategoryDto(
                "Updated_Category",
                "Updated_Description"
        );

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setName("Updated_Category");
        updatedCategory.setDescription("Updated_Description");

        Mockito.when(categoryService.updateCategory(
                        eq(categoryId), any(CategoryDto.class)))
                .thenReturn(updatedCategory);

        mockMvc.perform(put("/api/user/updateCategory/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("redirect:/auth/user/allCategories"));

        ArgumentCaptor<CategoryDto> captor =
                ArgumentCaptor.forClass(CategoryDto.class);

        Mockito.verify(categoryService)
                .updateCategory(eq(categoryId), captor.capture());

        CategoryDto captured = captor.getValue();

        assertEquals(updatedCategory.getName(), captured.getName());
        assertEquals(updatedCategory.getDescription(), captured.getDescription());
    }


   /* @Test
    void shouldUpdateCategory() throws Exception {

        CategoryDto requestDto =
                new CategoryDto("Updated Chair", "Updated Seating");

        CategoryDto responseDto =
                new CategoryDto("Updated Chair", "Updated Seating");

        Mockito.when(categoryService.updateCategory(eq(1L), any(CategoryDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/user/updateCategory/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Chair"))
                .andExpect(jsonPath("$.description").value("Updated Seating"));
    }*/

    // Delete category
    @Test
    void shouldDeleteCategoryAndRedirect() throws Exception {

        Long categoryId = 1L;

        Mockito.when(categoryService.deleteCategory(categoryId))
                .thenReturn(true);

        mockMvc.perform(delete("/api/user/deleteCategory/{id}", categoryId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("status", true))
                .andExpect(redirectedUrl("/auth/user/allCategories"));
    }
}


    // Get All Categories
    /*@Test
    void shouldGetAllCategories() {
        int beforeCount = categoryRepository.findAll().size();
        categoryRepository.saveAll(Arrays.asList(
                new Category("Chair", "Seating"),
                new Category("Table", "Dining")
        ));

        ResponseEntity<Category[]> response =
                restTemplate.getForEntity("/api/user/allCategories", Category[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(beforeCount + 2, response.getBody().length);
    }

    // Get Category By Id
    @Test
    void shouldGetCategoryById() {

        Category saved = categoryRepository.save(
                new Category("Modular sheets", "Kitchen")
        );

        ResponseEntity<Category> response =
                restTemplate.getForEntity(
                        "/api/user/category/" + saved.getCategoryId(),
                        Category.class
                );

        // Print to console
        System.out.println("HTTP Status: " + response.getStatusCode());
        System.out.println("Category Id: " + response.getBody().getCategoryId());
        System.out.println("Category Name: " + response.getBody().getName());
        System.out.println("Category Description: " + response.getBody().getDescription());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved.getName(), response.getBody().getName());
    }

    // Update Category
    @Test
    void shouldUpdateCategory() {

        Category saved = categoryRepository.save(
                new Category("Old tv", "Old hall")
        );

        CategoryDto updatedDto =
                new CategoryDto("Television Panel", "Hall Furniture");

        HttpEntity<CategoryDto> request = new HttpEntity<>(updatedDto);

        ResponseEntity<CategoryDto> response =
                restTemplate.exchange(
                        "/api/user/updateCategory/" + saved.getCategoryId(),
                        HttpMethod.PUT,
                        request,
                        CategoryDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto.getName(), response.getBody().getName());
    }

    // Delete Category
    @Test
    void shouldDeleteCategory() {
        Category saved = categoryRepository.save(
                new Category("ToDelete", "Temp")
        );

        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/user/deleteCategory/" + saved.getCategoryId(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(categoryRepository.findById(saved.getCategoryId()).isPresent());
    }






    @Test
    void testRedirectWithFlashAttributes() throws Exception {
        mockMvc.perform(get("/api/user/createCategory"))
                .andExpect(status().is3xxRedirection()) // Check for 302
                .andExpect(redirectedUrl("/api/user/allProduct")) // Check redirect target
                .andExpect(flash().attribute("message", "Success!")); // Check passed data
    }


    @Test
    void shouldCreateCategory() throws Exception {

        CategoryDto requestDto =
                new CategoryDto("Test_CategoryName", "Test_CategoryDescription");

        Category saved = new Category();
        saved.setCategoryId(1L);
        saved.setName("Test_CategoryName");
        saved.setDescription("Test_CategoryDescription");

        Mockito.when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/user/createCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                // id generated dynamically then don't know what is actual id came
                .andExpect(jsonPath("$.categoryId").isNumber())
                .andExpect(jsonPath("$.name").value(requestDto.getName()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));

    }

    @Test
    void createCategory_Success_ShouldRedirectToAllProducts() throws Exception {
        // Arrange: Create a mock category to be returned by the service
        CategoryDto requestDto =
                new CategoryDto("Test_CategoryName", "Test_CategoryDescription");
        Category saved = new Category();
        saved.setCategoryId(1L);
        saved.setName("Test_CategoryName");
        saved.setDescription("Test_CategoryDescription");

        Mockito.when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(saved);
        // Act & Assert
        mockMvc.perform(post("/api/user/createCategory")

                        .param("name", "Switch") // Simulate form data for @ModelAttribute
                        .param("description", "Electronics"))
                .andExpect(status().is3xxRedirection()) // Check for 302 Found
                .andExpect(redirectedUrl("/api/user/allProducts")) // Check destination
                .andExpect(flash().attribute("category", requestDto)); // Verify Flash Attribute
    }

    @Test
    void shouldReturnCategoryWithCorrectValues() throws Exception {

        Category saved = new Category();
        saved.setCategoryId(1L);
        saved.setName("Test_Category");
        saved.setDescription("Test_Description");

        Mockito.when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/user/createCategory")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Test_Category")
                        .param("description", "Test_Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/user/home"))
                .andExpect(flash().attributeExists("category"))
                .andExpect(flash().attribute("category",
                        org.hamcrest.Matchers.hasProperty("name",
                                org.hamcrest.Matchers.is("Test_Category"))))
                .andExpect(flash().attribute("category",
                        org.hamcrest.Matchers.hasProperty("description",
                                org.hamcrest.Matchers.is("Test_Description"))));
    }
    */

