package com.ghassene.store.mappers;

import com.ghassene.store.dtos.CartDto;
import com.ghassene.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
