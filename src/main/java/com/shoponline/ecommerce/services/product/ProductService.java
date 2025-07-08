package com.shoponline.ecommerce.services.product;

import com.shoponline.ecommerce.dtos.product.UpsertProductDto;
import com.shoponline.ecommerce.dtos.product.ProductDto;
import com.shoponline.ecommerce.entities.Category;
import com.shoponline.ecommerce.entities.Product;
import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.exceptions.ResourceNotFoundException;
import com.shoponline.ecommerce.exceptions.ValidationException;
import com.shoponline.ecommerce.mappers.ProductMapper;
import com.shoponline.ecommerce.repositories.CategoryRepository;
import com.shoponline.ecommerce.repositories.ProductRepository;
import com.shoponline.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductDto> getByCategory(Long categoryId) {
        List<Product> products = categoryId != null
                ? productRepo.findByCategoryId(categoryId)
                : productRepo.findAllWithCategory();

        return products.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAll(Pageable pageable) {
        return productRepo.findByIsActiveTrue(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        Product product = productRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + id));

        if (!product.getIsActive()) {
            throw new ResourceNotFoundException("product is not available");
        }

        return mapper.toDto(product);
    }

    public ProductDto create(UpsertProductDto request, Long userId) {
        User user = findUser(userId);
        Category category = findCategory(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .createdBy(user)
                .isActive(true)
                .build();

        Product saved = productRepo.save(product);
        user.addCreatedProduct(saved);

        return mapper.toDto(saved);
    }

    public ProductDto update(Long productId, UpsertProductDto request, Long userId) {
        User user = findUser(userId);
        Product product = findProduct(productId);
        Category category = findCategory(request.getCategoryId());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        return mapper.toDto(productRepo.save(product));
    }

    public void delete(Long productId, Long userId) {
        User user = findUser(userId);
        Product product = findProduct(productId);

        user.removeCreatedProduct(product);
        productRepo.delete(product);
        log.info("Product deleted: {}", productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getByUser(Long userId, Pageable pageable, Boolean activeOnly) {
        User user = findUser(userId);

        Page<Product> products = Boolean.TRUE.equals(activeOnly)
                ? productRepo.findActiveByCreatedBy(user, pageable)
                : productRepo.findByCreatedBy(user, pageable);

        return products.map(mapper::toDto);
    }

    public void updateStock(Long productId, Integer quantity, Long userId) {
        if (quantity < 0) {
            throw new ValidationException("stock quantity cannot be negative");
        }

        User user = findUser(userId);
        Product product = findProduct(productId);

        product.setStockQuantity(quantity);
        productRepo.save(product);
    }

    private User findUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + userId));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category not found with id: " + categoryId));
    }

    private Product findProduct(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + productId));
    }
}