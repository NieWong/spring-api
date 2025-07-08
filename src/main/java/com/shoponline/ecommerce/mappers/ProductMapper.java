package com.shoponline.ecommerce.mappers;

import com.shoponline.ecommerce.dtos.product.ProductDto;
import com.shoponline.ecommerce.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    @Mapping(source = "createdBy.name", target = "createdByUserName")
    @Mapping(source = "createdBy.role", target = "createdByUserRole")
    @Mapping(target = "inStock", expression = "java(product.isInStock())")
    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);
}