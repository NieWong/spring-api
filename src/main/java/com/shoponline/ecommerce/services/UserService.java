package com.shoponline.ecommerce.services;

import com.shoponline.ecommerce.dtos.ChangePasswordRequest;
import com.shoponline.ecommerce.dtos.RegisterUserRequest;
import com.shoponline.ecommerce.dtos.UpdateUserRequest;
import com.shoponline.ecommerce.dtos.UserDto;
import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.exceptions.EmailAlreadyExistsException;
import com.shoponline.ecommerce.exceptions.InvalidPasswordException;
import com.shoponline.ecommerce.exceptions.ResourceNotFoundException;
import com.shoponline.ecommerce.mappers.UserMapper;
import com.shoponline.ecommerce.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(Pageable pageable, String search) {
        Page<User> users;
        if(StringUtils.hasText(search)) {
            users = userRepository.findByNameOrEmail(search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return userMapper.toDto(user);
    }

    public UserDto createUser(RegisterUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("email already exists" + request.getEmail());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if(!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("email already exists");
        }

        userMapper.update(request, user);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        userRepository.delete(user);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
