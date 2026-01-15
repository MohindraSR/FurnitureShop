package com.fs.main.controller;

import com.fs.main.config.jwt.JwtUtil;
import com.fs.main.dto.CategoryDto;
import com.fs.main.dto.ProductDto;
import com.fs.main.entity.Category;
import com.fs.main.entity.Product;
import com.fs.main.service.CategoryService;
import com.fs.main.service.CustomerService;
import com.fs.main.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/furniture")
public class FurnitureShopController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;


    // **Categories Entity Operations**
    /**
     * Purpose: Get All Categories list
     * @return
     */
    @GetMapping("/allCategories")
    public List<CategoryDto> getAllCategory(){
        return categoryService.getAllCategories();
    }

    /**
     * Purpose: Get Category detail as per category id
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public CategoryDto getCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/createCategory")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto){
        return categoryService.createCategory(categoryDto);
    }



    /**
     * Purpose: Update Category as per id
     * @param id
     * @param categoryDto
     * @return
     */
    @PutMapping("/updateCategory/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(id, categoryDto);
    }

    /**
     * Purpose: Delete category by id
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ** Product Entity Operations**

    /**
     * purpose: Get all product list
     * @return
     */
    @GetMapping("/allProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/getProductById/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping("/createProduct")
    public Product createProduct(@Valid @RequestBody ProductDto productDto){
        return productService.createProduct(productDto);
    }

    @PutMapping("/updateProduct/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @Valid @RequestBody ProductDto productDto){
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
