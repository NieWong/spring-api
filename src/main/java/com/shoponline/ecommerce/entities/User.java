// src/main/java/com/shoponline/ecommerce/entities/User.java
package com.shoponline.ecommerce.entities;

import com.shoponline.ecommerce.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Product> createdProducts = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Profile profile;

    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Builder.Default
    private Set<Product> favoriteProducts = new HashSet<>();

    // Helper methods
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

    public void addCreatedProduct(Product product) {
        createdProducts.add(product);
        product.setCreatedBy(this);
    }

    public void removeCreatedProduct(Product product) {
        createdProducts.remove(product);
        product.setCreatedBy(null);
    }

    public void addFavoriteProduct(Product product) {
        favoriteProducts.add(product);
    }

    public void removeFavoriteProduct(Product product) {
        favoriteProducts.remove(product);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isSeller() {
        return !this.createdProducts.isEmpty();
    }

    public boolean canCreateProducts() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ", " +
                "role = " + role + ")";
    }
}