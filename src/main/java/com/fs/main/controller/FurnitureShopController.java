package com.fs.main.controller;

import com.fs.main.dto.CategoryDto;
import com.fs.main.dto.ProductDto;
import com.fs.main.entity.Category;
import com.fs.main.entity.Product;
import com.fs.main.service.CategoryService;
import com.fs.main.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class FurnitureShopController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;


    // **Categories Entity Operations**

    /**
     * Purpose: Create new Category
     * @param categoryDto
     * @return
     */
   // @PostMapping("/createCategory")
  /*  public Category createCategory(@Valid @RequestBody CategoryDto categoryDto){
        return categoryService.createCategory(categoryDto);
    }*/

    @PostMapping("/createCategory")
    public String createCategory(
             @Valid @ModelAttribute CategoryDto categoryDto,
             RedirectAttributes redirectAttributes) {
        Category category = categoryService.createCategory(categoryDto);

        if (category != null) {
            return "redirect:/api/user/allProducts";
        }
        redirectAttributes.addFlashAttribute("error", "Something went wrong");
        return "redirect:/api/user/createCategory";
    }


    /**
     * Purpose: Get All Categories list
     * @return
     */
    @GetMapping("/allCategories")
    public ModelAndView getAllCategory(ModelAndView modelAndView){
        List<Category> categoryList = categoryService.getAllCategories();
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.setViewName("CustomerHomePage");
        return modelAndView;
    }

    /**
     * Purpose: Get Category detail as per category id
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public ModelAndView getCategory(@PathVariable Long id, ModelAndView modelAndView){
         Category category = categoryService.getCategoryById(id);
         modelAndView.addObject("category", category);
         modelAndView.setViewName("CustomerHomePage");
        return modelAndView;
    }

    /**
     * Purpose: Update Category as per id
     * @param id
     * @param categoryDto
     * @return
     */
    @PutMapping("/updateCategory/{id}")
    public String updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody CategoryDto categoryDto,
                                 RedirectAttributes redirectAttributes){

         Category category = categoryService.updateCategory(id, categoryDto);

        return "redirect:/auth/user/allCategories";
    }

    /**
     * Purpose: Delete category by id
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCategory/{id}")
    public String  deleteCategory(@PathVariable Long id){
        boolean status = categoryService.deleteCategory(id);
        return "redirect:/auth/user/allCategories";
    }

    // ** Product Entity Operations**

    /**
     * Purpose: Create new product
     * @param productDto
     * @return
     */
    @PostMapping("/createProduct")
    public String  createProduct(@Valid @RequestBody ProductDto productDto){
        productService.createProduct(productDto);

        return "redirect:/auth/user/allProducts";
    }

    /*@GetMapping("/allProduct")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }*/
    /**
     * purpose: Get all product list
     * @return
     */
    @GetMapping("/allProducts")
    public ModelAndView getAllProduct(ModelAndView modelAndView){
        List<Product> productList = new ArrayList<>();
        productList = productService.getAllProducts();
        modelAndView.addObject("products", productList);
        modelAndView.setViewName("CustomerHomePage");
        return  modelAndView;
    }

    /**
     * Purpose: Get Product Detail by Id
     * @param id
     * @return
     */
    @GetMapping("/getProductById/{id}")
    public ModelAndView getProduct(@PathVariable Long id, ModelAndView modelAndView){
        Product product = productService.getProductById(id);
        modelAndView.addObject("productById", product);
        modelAndView.setViewName("CustomerHomePage");
        return modelAndView;
    }

    /**
     * Purpose: Update Product
     * @param id
     * @param productDto
     * @return
     */
    @PutMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id,
                                 @Valid @RequestBody ProductDto productDto){
         productService.updateProduct(id, productDto);
         return "redirect:/auth/user/allProducts";
    }

    /**
     * Purpose: Delete Product by Id
     * @param id
     * @return
     */
    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/auth/user/allProducts";
    }
}
