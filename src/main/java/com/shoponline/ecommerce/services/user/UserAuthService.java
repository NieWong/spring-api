package com.shoponline.ecommerce.services.user;

import com.shoponline.ecommerce.dtos.user.ChangePasswordRequest;
import com.shoponline.ecommerce.dtos.user.RegisterUserRequest;
import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.exceptions.InvalidPasswordException;
import com.shoponline.ecommerce.exceptions.ResourceNotFoundException;
import com.shoponline.ecommerce.mappers.UserMapper;
import com.shoponline.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public User createSecureUser(RegisterUserRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
}

