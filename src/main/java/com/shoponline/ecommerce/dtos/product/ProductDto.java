package com.shoponline.ecommerce.dtos.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean isActive;
    private Long categoryId;
    private String categoryName;
    private Long createdByUserId;
    private String createdByUserName;
    private String createdByUserRole;
    private Date createdAt;
    private Date updatedAt;
    private Boolean inStock;
}