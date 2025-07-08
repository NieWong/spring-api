package com.shoponline.ecommerce.services.user;

import com.shoponline.ecommerce.dtos.user.RegisterUserRequest;
import com.shoponline.ecommerce.dtos.user.UpdateUserRequest;
import com.shoponline.ecommerce.dtos.user.UserDto;
import com.shoponline.ecommerce.entities.User;
import com.shoponline.ecommerce.exceptions.EmailAlreadyExistsException;
import com.shoponline.ecommerce.exceptions.ResourceNotFoundException;
import com.shoponline.ecommerce.mappers.UserMapper;
import com.shoponline.ecommerce.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserAuthService userAuthService;

    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(Pageable pageable, String search) {
        Page<User> users = StringUtils.hasText(search)
                ? userRepository.search(search, pageable)
                : userRepository.findAll(pageable);

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
            throw new EmailAlreadyExistsException("email already exists: " + request.getEmail());
        }

        User savedUser = userAuthService.createSecureUser(request);
        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if(!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
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
}