package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findByNameOrEmail(String name, String email, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain%")
    Page<User> findByEmailDomain(@Param("domain") String domain, Pageable pageable);
}
