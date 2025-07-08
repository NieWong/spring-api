package com.shoponline.ecommerce.controllers;

import com.shoponline.ecommerce.dtos.product.ProductDto;
import com.shoponline.ecommerce.entities.Product;
import com.shoponline.ecommerce.mappers.ProductMapper;
import com.shoponline.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> getProducts(
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        List <Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else  {
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::toDto).toList();
    }
}
