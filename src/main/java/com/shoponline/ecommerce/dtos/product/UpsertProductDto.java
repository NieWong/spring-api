// src/main/java/com/shoponline/ecommerce/dtos/product/CreateProductRequest.java
package com.shoponline.ecommerce.dtos.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpsertProductDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId;
    private Boolean isActive;
}