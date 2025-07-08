// src/main/java/com/shoponline/ecommerce/repositories/CategoryRepository.java
package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}