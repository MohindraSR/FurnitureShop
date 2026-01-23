package com.fs.main.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    private Long productId;
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;


    public ProductDto(String name, String description, Double price, Integer stockQuantity, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
    }

    public ProductDto(Long productId, String name, String description, Double price, Integer stockQuantity, String categoryName) {
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.description = description;
        this.name = name;
        this.productId = productId;
    }

}
