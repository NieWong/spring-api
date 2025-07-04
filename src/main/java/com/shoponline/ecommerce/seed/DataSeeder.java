package com.shoponline.ecommerce.seed;

import com.shoponline.ecommerce.entities.Category;
import com.shoponline.ecommerce.entities.Product;
import com.shoponline.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        Category electronics = new Category("Electronics");
        Category fashion = new Category("Fashion");

        Product p1 = Product.builder()
                .name("iPhone 15")
                .description("Smart phone")
                .price(new BigDecimal("1299.99"))
                .category(electronics)
                .build();

        Product p2 = Product.builder()
                .name("T-Shirt")
                .description("Cotton tee")
                .price(new BigDecimal("19.99"))
                .category(fashion)
                .build();

        productRepository.save(p1);
        productRepository.save(p2);
    }
}
