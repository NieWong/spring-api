package com.shoponline.ecommerce.mappers;

import com.shoponline.ecommerce.dtos.ProductDto;
import com.shoponline.ecommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}

