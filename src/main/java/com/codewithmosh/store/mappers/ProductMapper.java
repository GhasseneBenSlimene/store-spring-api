package com.ghassenebenslimene.store.mappers;

import com.ghassenebenslimene.store.dtos.ProductDto;
import com.ghassenebenslimene.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);
}
