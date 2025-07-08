package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.Product;
import com.shoponline.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();
    Page<Product> findByCreatedBy(User createdBy, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.createdBy = :user AND p.isActive = true")
    Page<Product> findActiveByCreatedBy(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.createdBy " +
            "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);

    long countByCreatedBy(User user);

    @EntityGraph(attributePaths = {"category", "createdBy"})
    List<Product> findByCreatedByOrderByCreatedAtDesc(User createdBy);

    @EntityGraph(attributePaths = {"category", "createdBy"})
    Page<Product> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 AND p.isActive = true")
    @EntityGraph(attributePaths = {"category", "createdBy"})
    Page<Product> findInStockProducts(Pageable pageable);
}