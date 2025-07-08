package com.shoponline.ecommerce.services.user;


import com.shoponline.ecommerce.entities.Product;
import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.exceptions.ResourceNotFoundException;
import com.shoponline.ecommerce.repositories.ProductRepository;
import com.shoponline.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWishListService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void addToFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));

        user.addFavoriteProduct(product);
        userRepository.save(user);
    }
}
