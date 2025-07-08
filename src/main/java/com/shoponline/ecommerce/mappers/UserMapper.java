package com.shoponline.ecommerce.mappers;

import com.shoponline.ecommerce.dtos.user.RegisterUserRequest;
import com.shoponline.ecommerce.dtos.user.UpdateUserRequest;
import com.shoponline.ecommerce.dtos.user.UserDto;
import com.shoponline.ecommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "favoriteProducts", ignore = true)
    User toEntity(RegisterUserRequest request);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "favoriteProducts", ignore = true)
    void update(UpdateUserRequest request, @MappingTarget User user);}

