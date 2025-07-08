package com.shoponline.ecommerce.repositories;

import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.enums.Role;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"profile"})
    @NonNull
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"profile"})
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain%")
    Page<User> findByEmailDomain(@Param("domain") String domain, Pageable pageable);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);

    List<User> findByIsActiveTrue();

    Page<User> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.role = :role")
    List<User> findActiveUsersByRole(@Param("role") Role role);

    @EntityGraph(attributePaths = {"profile"})
    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:role IS NULL OR u.role = :role)")
    Page<User> searchWithRole(@Param("search") String search, @Param("role") Role role, Pageable pageable);

    long countByRole(Role role);

    long countByIsActiveTrue();

    @Query("SELECT u FROM User u WHERE u.role IN ('SELLER', 'ADMIN') AND u.isActive = true")
    List<User> findProductCreators();
}